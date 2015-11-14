package com.getMyParking.dao;

import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.AccessMasterEntity;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.reports.ParkingReport;
import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.getMyParking.entity.reports.ParkingReportBySubLotType;
import com.getMyParking.entity.reports.ParkingReportByUser;
import com.google.common.base.Function;
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
import org.hibernate.type.StringType;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingEventDAO extends AbstractDAO<ParkingEventEntity> {
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

        Query q = currentSession().createQuery("from ParkingEventEntity where parkingSubLot.id =:id and updatedTime >= :updatedTime");
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

        return list(query);
    }

    public List<ParkingEventEntity> findBySerialNumberAndEventType(int parkingLotId, String eventType, String serialNumber) {
        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class);
        criteria.add(Restrictions.eq("eventType",eventType))
                .add(Restrictions.eq("parkingSubLot.id",parkingLotId))
                .add(Restrictions.eq("serialNumber", serialNumber));

        return list(criteria);
    }

    public ParkingReport createParkingSubLotReport(Integer parkingSubLotId, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingSubLot.id",parkingSubLotId),fromDate,toDate,null);
    }

    public ParkingReport createParkingLotReport(Integer parkingLotId, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingLotId", parkingLotId),fromDate,toDate,null);
    }

    public ParkingReport createParkingReport(Integer parkingId, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingId", parkingId),fromDate,toDate,null);
    }

    public ParkingReport createCompanyReport(Integer companyId, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("companyId", companyId),fromDate,toDate,null);
    }

    public ParkingReport createUserReport(String operatorName, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("operatorName", operatorName),fromDate,toDate,null);
    }

    public ParkingReport createReport(Criterion fetchCriteria, DateTime fromDate, DateTime toDate, String type) {

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.rowCount());
        projectionList.add(Projections.sum("cost"));

        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_IN"))
                .setProjection(projectionList);
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        List<Object[]> list = criteria.list();
        Object[] row = list.get(0);

        Integer checkInCount = 0;
        if (row != null) checkInCount = ((Long)row[0]).intValue();

        BigDecimal checkInRevenue = null;
        if (row != null) checkInRevenue = (BigDecimal) row[1];
        if (checkInRevenue == null) checkInRevenue = new BigDecimal(0);

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .setProjection(projectionList);
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        list = criteria.list();
        row = list.get(0);

        Integer checkOutCount = 0;
        if (row != null) checkOutCount = ((Long)row[0]).intValue();

        BigDecimal checkOutRevenue = null;
        if (row != null) checkOutRevenue = (BigDecimal) row[1];
        if (checkOutRevenue == null) checkOutRevenue = new BigDecimal(0);

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .add(Restrictions.eq("special","FOC"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        Long focCount = (Long) criteria.list().get(0);
        if (focCount == null) focCount = 0L;

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .add(Restrictions.eq("special","TT"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        Long ttCount = (Long) criteria.list().get(0);
        if (ttCount == null) ttCount = 0L;

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .add(Restrictions.eq("special", "AC"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        Long acCount = (Long) criteria.list().get(0);
        if (acCount == null) acCount = 0L;

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_IN"))
                .add(Restrictions.eq("type","PASS"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        Long passCheckInCount = (Long) criteria.list().get(0);
        if (passCheckInCount == null) passCheckInCount = 0L;

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .add(Restrictions.eq("type","PASS"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        Long passCheckOutCount = (Long) criteria.list().get(0);
        if (passCheckOutCount == null) passCheckOutCount = 0L;

        return new ParkingReport(checkInCount,checkOutCount,
                focCount.intValue(),ttCount.intValue(),
                passCheckInCount.intValue(),passCheckOutCount.intValue(),
                checkInRevenue,checkOutRevenue, acCount.intValue());

    }

    public List<ParkingReportBySubLotType> createParkingReportByTypes(Integer parkingId, DateTime from, DateTime to,
                                                               List<String> typesList) {

        List<ParkingReportBySubLotType> parkingReportGroup = Lists.newArrayList();

        for (LocalDate date = from.toLocalDate(); date.isBefore(to.toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            List<ParkingReport> parkingReports = Lists.newArrayList();
            for (String type : typesList) {
                ParkingReport parkingReport =
                        createReport(Restrictions.eq("parkingId",parkingId),date.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5, 30)),
                                date.plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5,30)),type);
                parkingReport.setType(type);
                parkingReports.add(parkingReport);
            }
            parkingReportGroup.add(new ParkingReportBySubLotType(date,parkingReports));
        }
        return parkingReportGroup;
    }

    public List<ParkingReportByUser> createParkingReportByUsers(DateTime fromDateTime, DateTime toDateTime,
                                                                     List<ParkingSubLotUserAccessEntity> users) {

        List<ParkingSubLotUserAccessEntity> filteredUsers = users.stream().filter(userAccessEntity ->
                userAccessEntity.getUserB2B().getUserAccesses().stream().map(
                        AccessMasterEntity::getAccessTitle
                ).anyMatch(
                        s -> s.equalsIgnoreCase("CHECKED_IN") || s.equalsIgnoreCase("CHECKED_OUT")
                )).collect(Collectors.toList());

        Map<String,ParkingReportByUser> parkingReports = Maps.newHashMap();
        for (ParkingSubLotUserAccessEntity user : filteredUsers) {
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
                    createReport(Restrictions.and(Restrictions.eq("parkingSubLot.id", user.getParkingSubLotId()),
                            Restrictions.eq("operatorName",username)),fromDateTime,toDateTime,null);
            parkingReport.setParkingSubLotId(user.getParkingSubLotId());
            userParkingReport.getParkingReports().add(parkingReport);
        }

        return Lists.newArrayList(parkingReports.values());
    }

    /*
    public ParkingReport createReport(Criterion fetchCriteria, DateTime fromDate, DateTime toDate, String type) {

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.rowCount(),"count");
        projectionList.add(Projections.sum("cost"),"revenue");
        projectionList.add(Projections.groupProperty("type"),"parkingType");
        projectionList.add(Projections.groupProperty("eventType"),"eventType");
        projectionList.add(Projections.groupProperty("subLotType"),"subLotType");

        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(projectionList);
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));

        criteria.setResultTransformer(Transformers.aliasToBean(ReportDetails.class));

        return new ParkingReport(criteria.list());

    }

    public List<ParkingReportBySubLotType> createParkingReportByTypes(Integer parkingId, DateTime from, DateTime to,
                                            List<String> typesList) {

        List<ParkingReportBySubLotType> parkingReportGroup = Lists.newArrayList();

        for (LocalDate date = from.toLocalDate(); date.isBefore(to.toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.rowCount(),"count");
            projectionList.add(Projections.sum("cost"),"revenue");
            projectionList.add(Projections.groupProperty("eventType"),"eventType");
            projectionList.add(Projections.groupProperty("subLotType"),"subLotType");

            Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class)
                    .add(Restrictions.between("eventTime", date.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5, 30)),
                            date.plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5, 30))))
                    .add(Restrictions.eq("parkingId", parkingId))
                    .setProjection(projectionList);
            criteria.setResultTransformer(Transformers.aliasToBean(SubLotReportDetails.class));
            List<SubLotReportDetails> reportDetails = criteria.list();
            parkingReportGroup.add(new ParkingReportBySubLotType(date,reportDetails));
        }
        return parkingReportGroup;
    }

    public List<ParkingReportByUser> createParkingReportByUsers(DateTime fromDateTime, DateTime toDateTime,
                                                               List<ParkingSubLotUserAccessEntity> users) {

        List<ParkingSubLotUserAccessEntity> filteredUsers = users.stream().filter(userAccessEntity ->
                userAccessEntity.getUserB2B().getUserAccesses().stream().map(
                    AccessMasterEntity::getAccessTitle
                ).anyMatch(
                        s -> s.equalsIgnoreCase("CHECKED_IN") || s.equalsIgnoreCase("CHECKED_OUT")
                )).collect(Collectors.toList());

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.rowCount(),"count");
        projectionList.add(Projections.sum("cost"),"revenue");
        projectionList.add(Projections.groupProperty("special"), "special");
        projectionList.add(Projections.groupProperty("eventType"),"eventType");
        projectionList.add(Projections.groupProperty("parkingSubLot.id"),"parkingSubLotId");
        projectionList.add(Projections.groupProperty("operatorName"),"operatorName");

        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.between("eventTime", fromDateTime, toDateTime))
                .add(Restrictions.in("operatorName",
                        filteredUsers.stream().map(user -> user.getUserB2B().getUsername()).collect(Collectors.toList())))
                .add(Restrictions.in("parkingSubLot.id",
                        filteredUsers.stream().map(ParkingSubLotUserAccessEntity::getParkingSubLotId).collect(Collectors.toList())))
                .setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(UserParkingReportDetails.class));
        List<UserParkingReportDetails> detailsList = criteria.list();

        Map<String,List<UserParkingReportDetails>> userDetailsMap =
                detailsList.stream().collect(Collectors.groupingBy(UserParkingReportDetails::getOperatorName));

        Map<String,List<ParkingSubLotUserAccessEntity>> userAccessEntityMap =
                filteredUsers.stream().collect(Collectors.groupingBy(user -> user.getUserB2B().getUsername()));
        List<ParkingReportByUser> parkingReports = Lists.newArrayList();
        userDetailsMap.forEach((user, reportDetails) -> {
            ParkingSubLotUserAccessEntity userAccess = userAccessEntityMap.get(user).get(0);
            ParkingReportByUser userParkingReport = new ParkingReportByUser();
            userParkingReport.setUsername(user);
            userParkingReport.setCompanyId(userAccess.getCompanyId());
            userParkingReport.setParkingId(userAccess.getParkingId());
            userParkingReport.setParkingLotId(userAccess.getParkingLotId());
            userParkingReport.setReportDetails(reportDetails);
            parkingReports.add(userParkingReport);
        });
        return parkingReports;
    } */

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
            criteria.add(Restrictions.eq("parkingSubLot.id",parkingSubLotId.get().get()));
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

        DateTime startDateTime = date.toLocalDate().toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5, 30));
        DateTime endDateTime = startDateTime.plusDays(1).minusSeconds(1);

        SQLQuery checkInEventsQuery = currentSession().createSQLQuery("select `pe`.`registration_number` as registrationNumber, " +
                "`pe`.`mobile_number` as mobileNumber, `pe`.`valet_name` as valetName, `pe`.`sub_lot_type` as subLotType," +
                "`pe`.`serial_number` as serialNumber, `pe`.`special` as special, `pe`.`operator_name` as operatorName," +
                "`parking_pass`.`valid_time` as passValidTime, `pe`.`event_time` as checkInEventTime , `pe`.`cost` as checkInCost," +
                "`jpe`.`event_time` as checkOutEventTime, `jpe`.`cost` as checkOutCost, `pe`.`event_time` as eventTime from `parking_event` pe " +
                "left join `parking_event` jpe on pe.`serial_number` = jpe.`serial_number` and pe.`event_type` != jpe.`event_type`" +
                "left join `parking_pass` on pe.`parking_pass_id` = `parking_pass`.`id` " +
                "where pe.`event_time` >= :startDate and pe.`event_time` <= :endDate " +
                "and pe.`event_type` = 'CHECKED_IN' and pe.`parking_id`= :parkingId");


        checkInEventsQuery.setParameter("parkingId", parkingId);
        checkInEventsQuery.setParameter("startDate", startDateTime.toString());
        checkInEventsQuery.setParameter("endDate", endDateTime.toString());
        addDumpScalars(checkInEventsQuery);
        checkInEventsQuery.setResultTransformer(Transformers.aliasToBean(ParkingEventDumpDTO.class));

        List<ParkingEventDumpDTO> checkInEvents = checkInEventsQuery.list();

        SQLQuery checkOutEventsQuery = currentSession().createSQLQuery("select `pe`.`registration_number` as registrationNumber, " +
                "`pe`.`mobile_number` as mobileNumber, `pe`.`valet_name` as valetName, `pe`.`sub_lot_type` as subLotType," +
                "`pe`.`serial_number` as serialNumber, `pe`.`special` as special, `pe`.`operator_name` as operatorName," +
                "`parking_pass`.`valid_time` as passValidTime, `pe`.`event_time` as checkInEventTime , `pe`.`cost` as checkInCost," +
                "`jpe`.`event_time` as checkOutEventTime, `jpe`.`cost` as checkOutCost, `pe`.`event_time` as eventTime from `parking_event` pe " +
                "left join `parking_event` jpe on pe.`serial_number` = jpe.`serial_number` and pe.`event_type` != jpe.`event_type`" +
                "left join `parking_pass` on pe.`parking_pass_id` = `parking_pass`.`id` " +
                "where pe.`event_time` >= :startDate and pe.`event_time` <= :endDate " +
                "and pe.`event_type` = 'CHECKED_OUT' and pe.`parking_id`= :parkingId");

        checkOutEventsQuery.setParameter("parkingId",parkingId);
        checkOutEventsQuery.setParameter("startDate",startDateTime.toString());
        checkOutEventsQuery.setParameter("endDate", endDateTime.toString());
        addDumpScalars(checkOutEventsQuery);
        checkOutEventsQuery.setResultTransformer(Transformers.aliasToBean(ParkingEventDumpDTO.class));

        List<ParkingEventDumpDTO> checkOutEvents = checkOutEventsQuery.list();
        checkOutEvents.stream().filter(parkingEvent -> parkingEvent.getCheckInEventTime().isBefore(startDateTime))
                .forEach(parkingEvent -> parkingEvent.setCheckInCost(null));

        checkInEvents.addAll(checkOutEvents);
        Collections.sort(checkInEvents);

        return checkInEvents;


    }

    private SQLQuery addDumpScalars(SQLQuery query) {
        query.addScalar("registrationNumber", StringType.INSTANCE);
        query.addScalar("mobileNumber", StringType.INSTANCE);
        query.addScalar("valetName", StringType.INSTANCE);
        query.addScalar("checkInEventTime", new CustomType(new PersistentDateTime()));
        query.addScalar("checkInCost", BigDecimalType.INSTANCE);
        query.addScalar("subLotType", StringType.INSTANCE);
        query.addScalar("checkOutEventTime", new CustomType(new PersistentDateTime()));
        query.addScalar("checkOutCost", BigDecimalType.INSTANCE);
        query.addScalar("serialNumber", StringType.INSTANCE);
        query.addScalar("special", StringType.INSTANCE);
        query.addScalar("operatorName", StringType.INSTANCE);
        query.addScalar("passValidTime", new CustomType(new PersistentDateTime()));
        query.addScalar("eventTime", new CustomType(new PersistentDateTime()));
        return query;
    }
}
