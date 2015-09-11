package com.getMyParking.dao;

import com.getMyParking.entity.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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

    public ParkingReport createReport(ParkingSubLotEntity parkingSubLot, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingSubLot.id",parkingSubLot.getId()),fromDate,toDate,null);
    }

    public ParkingReport createReport(ParkingLotEntity parkingLot, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingLotId", parkingLot.getId()),fromDate,toDate,null);
    }

    public ParkingReport createReport(ParkingEntity parking, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingId", parking.getId()),fromDate,toDate,null);
    }

    public ParkingReport createReport(CompanyEntity company, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("companyId", company.getId()),fromDate,toDate,null);
    }

    public ParkingReport createReport(Criterion fetchCriteria, DateTime fromDate, DateTime toDate, String type) {

        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_IN"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        List list = criteria.list();

        Integer checkInCount = 0;
        if (list != null) checkInCount = ((Long)list.get(0)).intValue();

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .setProjection(Projections.rowCount());
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        list = criteria.list();

        Integer checkOutCount = 0;
        if (list != null) checkOutCount = ((Long)list.get(0)).intValue();

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("eventType", "CHECKED_IN"));
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        list = criteria.list();

        BigDecimal checkInRevenue = (BigDecimal) list.get(0);
        if (checkInRevenue == null) checkInRevenue = new BigDecimal(0);

        criteria = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"));
        if (type != null) criteria.add(Restrictions.eq("subLotType",type));
        list = criteria.list();

        BigDecimal checkOutRevenue = (BigDecimal) list.get(0);
        if (checkOutRevenue == null) checkOutRevenue = new BigDecimal(0);

        return new ParkingReport(checkInCount,checkOutCount,checkInRevenue,checkOutRevenue);

    }

    public List<ParkingReportGroup> createReport(ParkingEntity parking, DateTime from, DateTime to,
                                            List<String> typesList) {

        List<ParkingReportGroup> parkingReportGroup = Lists.newArrayList();

        for (LocalDate date = from.toLocalDate(); date.isBefore(to.toLocalDate().plusDays(1)); date = date.plusDays(1)) {
            List<ParkingReport> parkingReports = Lists.newArrayList();
            for (String type : typesList) {
                ParkingReport parkingReport =
                        createReport(Restrictions.eq("parkingId",parking.getId()),date.toDateTimeAtStartOfDay(),
                                date.plusDays(1).toDateTimeAtStartOfDay(),type);
                parkingReport.setType(type);
                parkingReports.add(parkingReport);
            }
            parkingReportGroup.add(new ParkingReportGroup(date,parkingReports));
        }
        return parkingReportGroup;
    }
}
