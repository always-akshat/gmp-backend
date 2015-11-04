package com.getMyParking.dao;

import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.*;
import com.getMyParking.entity.reports.*;
import com.google.common.collect.Lists;
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

        List<ParkingReportByUser> parkingReports = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity user : filteredUsers) {
            String username = user.getUserB2B().getUsername();
            ParkingReportByUser userParkingReport = new ParkingReportByUser();
            userParkingReport.setUsername(username);
            userParkingReport.setCompanyId(user.getCompanyId());
            userParkingReport.setParkingId(user.getParkingId());
            userParkingReport.setParkingLotId(user.getParkingLotId());
            userParkingReport.setReportDetails(userDetailsMap.get(username));
            parkingReports.add(userParkingReport);
        }
        return parkingReports;
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
            criteria.add(Restrictions.eq("parkingSubLot.id",parkingSubLotId.get().get()));
        }

        if (registrationNumber.isPresent()) {
            criteria.add(Restrictions.eq("registrationNumber",registrationNumber.get()));
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
        checkOutEventsQuery.setParameter("endDate",endDateTime.toString());
        addDumpScalars(checkOutEventsQuery);
        checkOutEventsQuery.setResultTransformer(Transformers.aliasToBean(ParkingEventDumpDTO.class));

        List<ParkingEventDumpDTO> checkOutEvents = checkOutEventsQuery.list();
        for (ParkingEventDumpDTO parkingEvent : checkOutEvents) {
            if (parkingEvent.getCheckInEventTime().isBefore(startDateTime)) {
                parkingEvent.setCheckInCost(null);
            }
        }

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
