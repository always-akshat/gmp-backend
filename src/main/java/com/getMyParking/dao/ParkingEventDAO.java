package com.getMyParking.dao;

import com.getMyParking.entity.*;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        list = criteria.list();
        row = list.get(0);

        Integer checkOutCount = 0;
        if (row != null) checkOutCount = ((Long)row[0]).intValue();

        BigDecimal checkOutRevenue = null;
        if (row != null) checkOutRevenue = (BigDecimal) row[1];
        if (checkOutRevenue == null) checkOutRevenue = new BigDecimal(0);

        return new ParkingReport(checkInCount,checkOutCount,checkInRevenue,checkOutRevenue);

    }

    public List<ParkingReportGroup> createParkingReportByTypes(Integer parkingId, DateTime from, DateTime to,
                                            List<String> typesList) {

        List<ParkingReportGroup> parkingReportGroup = Lists.newArrayList();

        for (LocalDate date = from.toLocalDate(); date.isBefore(to.toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            List<ParkingReport> parkingReports = Lists.newArrayList();
            for (String type : typesList) {
                ParkingReport parkingReport =
                        createReport(Restrictions.eq("parkingId",parkingId),date.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5, 30)),
                                date.plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.forOffsetHoursMinutes(5,30)),type);
                parkingReport.setType(type);
                parkingReports.add(parkingReport);
            }
            parkingReportGroup.add(new ParkingReportGroup(date,parkingReports));
        }
        return parkingReportGroup;
    }

    public List<ParkingReportGroupByUser> createParkingReportByUsers(DateTime fromDateTime, DateTime toDateTime,
                                                               List<ParkingSubLotUserAccessEntity> users) {

        List<ParkingSubLotUserAccessEntity> filteredUsers = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity user : users) {
            List<String> userAccess = Lists.transform(Lists.newArrayList(user.getUserB2B().getUserAccesses()),
                    new Function<AccessMasterEntity, String>() {
                        @Nullable
                        @Override
                        public String apply(AccessMasterEntity input) {
                            return input.getAccessTitle();
                        }
                    });

            if (userAccess.contains("CHECKED_IN") || userAccess.contains("CHECKED_OUT")) {
                filteredUsers.add(user);
            }
        }

        Map<String,ParkingReportGroupByUser> parkingReports = Maps.newHashMap();
        for (ParkingSubLotUserAccessEntity user : filteredUsers) {
            String username = user.getUserB2B().getUsername();
            ParkingReportGroupByUser userParkingReport;
            if (parkingReports.containsKey(username)) {
                userParkingReport =  parkingReports.get(username);
            } else {
                userParkingReport = new ParkingReportGroupByUser();
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

    public List<ParkingEventEntity> searchParkingEvents(Optional<IntParam> companyId, Optional<IntParam> parkingId,
                                                        Optional<IntParam> parkingLotId, Optional<IntParam> parkingSubLotId,
                                                        Optional<String> registrationNumber, Optional<DateTimeParam> fromDate,
                                                        Optional<DateTimeParam> toDate, Integer integer, Integer pageSize) {

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

        criteria.setFirstResult((integer-1)*pageSize);
        criteria.setMaxResults(pageSize);

        return list(criteria);

    }
}
