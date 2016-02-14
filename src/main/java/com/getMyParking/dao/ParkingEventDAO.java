package com.getMyParking.dao;

import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.AccessMasterEntity;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.getMyParking.entity.reports.ParkingReport;
import com.getMyParking.entity.reports.ParkingReportByQuery;
import com.getMyParking.entity.reports.ParkingReportByUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CustomType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingEventDAO extends AbstractDAO<ParkingEventEntity> {

    private static final Logger logger = LoggerFactory.getLogger(ParkingEventDAO.class);
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingEventDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateParkingEvent(ParkingEventEntity event) {
        persist(event);
    }


    public ParkingEventEntity findById(BigInteger id) {
        return get(id);
    }


    public List<ParkingEventEntity> getParkingEvents(int parkingSubLotId, DateTime lastUpdateTime) {

        Query q = currentSession().createQuery("from ParkingEventEntity where parkingSubLotId =:id and updatedTime >= :updatedTime");
        q.setInteger("id", parkingSubLotId);
        q.setString("updatedTime",lastUpdateTime.toString());
        return list(q);
    }

    public List<ParkingEventEntity> getAllParkingEventsByParking(Integer parkingId, DateTime fromDate, DateTime toDate) {
        return list(currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingId",parkingId))
                .add(Restrictions.between("eventTime", fromDate, toDate)));
    }


    public List<ParkingEventEntity> getParkingEvents(int parkingLotId, DateTime fromDate, DateTime toDate) {

        SQLQuery query = currentSession().createSQLQuery("SELECT * from `parking_event` where `event_type` = 'CHECKED_IN' " +
                "AND parking_sub_lot_id = :id AND`event_time` between :fromDate AND :toDate AND `serial_number` NOT IN " +
                "(Select `serial_number` from `parking_event` where `event_type` = 'CHECKED_OUT' AND " +
                "`event_time` between :fromDate AND :toDate AND parking_sub_lot_id = :id)");

        query.setParameter("id", parkingLotId);
        query.setParameter("fromDate", fromDate.toString());
        query.setParameter("toDate", toDate.toString());
        query.addEntity(ParkingEventEntity.class);
        query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return list(query);
    }

    public List<ParkingEventEntity> findBySerialNumberAndEventType(int parkingSubLotId, String eventType, String serialNumber) {
        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class);
        criteria.add(Restrictions.eq("eventType",eventType))
                .add(Restrictions.eq("parkingSubLotId",parkingSubLotId))
                .add(Restrictions.eq("serialNumber", serialNumber));

        return list(criteria);
    }

    public ParkingReport createParkingSubLotReport(Integer parkingSubLotId, DateTime fromDate, DateTime toDate) {
        return createReport("parking_sub_lot_id = " + parkingSubLotId,fromDate,toDate);
    }

    public ParkingReport createReport(String fetchCriteria, DateTime fromDate, DateTime toDate) {

        SQLQuery query = currentSession().createSQLQuery("select COUNT(*) as count, SUM(cost) as cost, `type` as type" +
                ", special as special, event_type as eventType from parking_event " +
                "where "+ fetchCriteria +" AND event_type in ('CHECKED_IN','CHECKED_OUT') AND `event_time` between :fromDate AND :toDate " +
                "Group by event_type, special, `type`");

        query.setParameter("toDate", toDate.toString());
        query.setParameter("fromDate", fromDate.toString());
        query.addScalar("count", LongType.INSTANCE);
        query.addScalar("cost", BigDecimalType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("special", StringType.INSTANCE);
        query.addScalar("eventType", StringType.INSTANCE);

        query.setResultTransformer(Transformers.aliasToBean(ParkingReportByQuery.class));

        List<ParkingReportByQuery> reports = query.list();
        Integer checkInCount = 0;
        Integer checkOutCount = 0;
        BigDecimal checkInRevenue = BigDecimal.ZERO;
        BigDecimal checkOutRevenue = BigDecimal.ZERO;
        Integer focCount = 0;
        Integer acCount = 0;
        Integer ttCount = 0;
        Integer passCheckInCount = 0;
        Integer passCheckOutCount = 0;
        for (ParkingReportByQuery report : reports) {
            if (report.getEventType().equalsIgnoreCase("CHECKED_IN")) {
                checkInCount += report.getCount().intValue();
                checkInRevenue = checkInRevenue.add(report.getCost());
                if (report.getType().equalsIgnoreCase("PASS")) {
                    passCheckInCount += report.getCount().intValue();
                }
            } else {
                checkOutCount += report.getCount().intValue();
                checkOutRevenue = checkOutRevenue.add(report.getCost());
                if (report.getType().equalsIgnoreCase("PASS")) {
                    passCheckOutCount += report.getCount().intValue();
                }
            }

            if (report.getSpecial() != null) {
                if (report.getSpecial().equalsIgnoreCase("FOC")) {
                    focCount += report.getCount().intValue();
                } else if (report.getSpecial().equalsIgnoreCase("TT")) {
                    ttCount += report.getCount().intValue();
                } else if (report.getSpecial().equalsIgnoreCase("AC")) {
                    acCount += report.getCount().intValue();
                }
            }
        }

        return new ParkingReport(checkInCount,checkOutCount, focCount, ttCount, passCheckInCount,
                passCheckOutCount, checkInRevenue,checkOutRevenue, acCount);

    }

    public List<ParkingReportByUser> createParkingReportByUsers(DateTime fromDateTime, DateTime toDateTime,
                                                                     List<ParkingSubLotUserAccessEntity> users) {

        Map<String,ParkingReportByUser> parkingReports = Maps.newHashMap();
        for (ParkingSubLotUserAccessEntity user : users) {
            String username = user.getUserB2B().getUsername();
            ParkingReportByUser userParkingReport;
            if (parkingReports.containsKey(username)) {
                userParkingReport =  parkingReports.get(username);
            } else {
                userParkingReport = new ParkingReportByUser();
                userParkingReport.setUsername(username);
                userParkingReport.setCompanyId(user.getCompanyId());
                userParkingReport.setParkingId(user.getParkingId());
                userParkingReport.setParkingLotId(user.getParkingLotId());
                userParkingReport.setParkingReports(new ArrayList<>());
                parkingReports.put(username,userParkingReport);
            }
            ParkingReport parkingReport =
                    createReport("parking_sub_lot_id = " + user.getParkingSubLotId() + " AND operator_name = '" + username +"' ",
            fromDateTime,toDateTime);
            parkingReport.setParkingSubLotId(user.getParkingSubLotId());
            userParkingReport.getParkingReports().add(parkingReport);
        }

        return Lists.newArrayList(parkingReports.values());
    }

    public void deleteSubLotByParkingId(Integer parkingId) {
        Query q = currentSession().createQuery("delete from ParkingEventEntity where parking_id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

    public List<ParkingEventEntity> searchParkingEvents(Optional<IntParam> companyId, Optional<IntParam> parkingId,
                                                        Optional<IntParam> parkingLotId, Optional<IntParam> parkingSubLotId,
                                                        Optional<String> registrationNumber, Optional<DateTimeParam> fromDate,
                                                        Optional<DateTimeParam> toDate, Optional<String> eventType, Integer pageNum,
                                                        Integer pageSize) {

        Criteria criteria = criteria();

        if (companyId.isPresent()) {
            criteria.add(Restrictions.eq("companyId",companyId.get().get()));
        }

        if (parkingId.isPresent()) {
            criteria.add(Restrictions.eq("parkingId",parkingId.get().get()));
        }

        if (parkingLotId.isPresent()) {
            criteria.add(Restrictions.eq("parkingLotId",parkingLotId.get().get()));
        }

        if (parkingSubLotId.isPresent()) {
            criteria.add(Restrictions.eq("parkingSubLotId",parkingSubLotId.get().get()));
        }

        if (registrationNumber.isPresent()) {
            criteria.add(Restrictions.ilike("registrationNumber",registrationNumber.get(), MatchMode.ANYWHERE));
        }

        if (fromDate.isPresent() && toDate.isPresent()) {
            criteria.add(Restrictions.between("eventTime",fromDate.get().get(),toDate.get().get()));
        }

        if (eventType.isPresent()) {
            criteria.add(Restrictions.eq("eventType", eventType.get()));
        }

        criteria.setFirstResult((pageNum - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        criteria.addOrder(Order.desc("eventTime"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return list(criteria);

    }

    public List<ParkingEventDumpDTO> getParkingEventsDump(Integer parkingId, DateTime date) {

        DateTime startDateTime = date.withTime(0, 0, 0, 0);
        DateTime endDateTime = startDateTime.plusDays(1).minusSeconds(1);

        logger.info("Parking event Dump Start Time {} , End Time {}", startDateTime.toString(), endDateTime.toString());

        SQLQuery query = currentSession().createSQLQuery("select `pe`.`registration_number` as registrationNumber," +
                "`pe`.`mobile_number` as mobileNumber, `pe`.`valet_name` as valetName, `pe`.`sub_lot_type` as subLotType," +
                "`pe`.`special` as special, `pe`.`operator_name` as eventOperatorName, `parking_pass`.`valid_time` as passValidTime," +
                "`pe`.`event_time` as eventTime, `pe`.`event_type` as eventType , `pe`.`cost` as eventCost, `jpe`.`event_type` as joinedEventType," +
                "`jpe`.`event_time` as joinedEventTime, `jpe`.`cost` as joinedEventCost, `jpe`.`operator_name` as joinedEventOperatorName," +
                "`pe`.`serial_number` as serialNumber from `parking_event` pe  left join `parking_event` jpe on" +
                "`pe`.`serial_number` = jpe.`serial_number` and pe.`event_type` != jpe.`event_type` left join `parking_pass`" +
                "on pe.`parking_pass_id` = `parking_pass`.`id`  where pe.`event_time` >= :startDate and pe.`event_time` <= :endDate " +
                "and pe.`parking_id`= :parkingId and `pe`.`event_type` in ('CHECKED_IN', 'CHECKED_OUT') group by `pe`.`serial_number`" +
                "order by `pe`.`event_time`");


        query.setParameter("parkingId", parkingId);
        query.setParameter("startDate", startDateTime.toString());
        query.setParameter("endDate", endDateTime.toString());
        query.addScalar("registrationNumber", StringType.INSTANCE);
        query.addScalar("mobileNumber", StringType.INSTANCE);
        query.addScalar("valetName", StringType.INSTANCE);
        query.addScalar("subLotType", StringType.INSTANCE);
        query.addScalar("serialNumber", StringType.INSTANCE);
        query.addScalar("special", StringType.INSTANCE);
        query.addScalar("passValidTime", new CustomType(new PersistentDateTime()));
        query.addScalar("eventTime", new CustomType(new PersistentDateTime()));
        query.addScalar("eventType", StringType.INSTANCE);
        query.addScalar("eventCost", BigDecimalType.INSTANCE);
        query.addScalar("joinedEventTime", new CustomType(new PersistentDateTime()));
        query.addScalar("joinedEventType", StringType.INSTANCE);
        query.addScalar("joinedEventCost", BigDecimalType.INSTANCE);
        query.addScalar("eventOperatorName", StringType.INSTANCE);
        query.addScalar("joinedEventOperatorName", StringType.INSTANCE);

        query.setResultTransformer(Transformers.aliasToBean(ParkingEventDumpDTO.class));

        List<ParkingEventDumpDTO> events = query.list();

        events.forEach(event -> {
            if (event.getEventType().equalsIgnoreCase("checked_in")) {
                event.setCheckInEventTime(event.getEventTime());
                event.setCheckInCost(event.getEventCost());
                event.setCheckinOperatorName(event.getEventOperatorName());
                event.setCheckOutEventTime(event.getJoinedEventTime());
                event.setCheckOutCost(event.getJoinedEventCost());
                event.setCheckoutOperatorName(event.getJoinedEventOperatorName());
            } else {
                event.setCheckOutEventTime(event.getEventTime());
                event.setCheckOutCost(event.getEventCost());
                event.setCheckoutOperatorName(event.getEventOperatorName());
                event.setCheckInEventTime(event.getJoinedEventTime());
                event.setCheckInCost(event.getJoinedEventCost());
                event.setCheckinOperatorName(event.getJoinedEventOperatorName());
            }
        });

        return events;
    }

    public ParkingEventEntity findByParkingPassId(Integer parkingPassId) {
        return uniqueResult(
                criteria()
                .add(Restrictions.eq("parkingPass.id", parkingPassId))
                .addOrder(Order.desc("eventTime"))
                .setMaxResults(1)
                .setFirstResult(0)
        );
    }
}
