package com.getMyParking.dao;

import com.getMyParking.entity.*;
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

import java.math.BigDecimal;
import java.util.List;

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


    public ParkingEventEntity findById(Integer id) {
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
        return createReport(Restrictions.eq("parkingSubLot.id",parkingSubLot.getId()),fromDate,toDate);
    }

    public ParkingReport createReport(ParkingLotEntity parkingSubLot, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingLotId",parkingSubLot.getId()),fromDate,toDate);
    }

    public ParkingReport createReport(ParkingEntity parkingSubLot, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("parkingId",parkingSubLot.getId()),fromDate,toDate);
    }

    public ParkingReport createReport(CompanyEntity parkingSubLot, DateTime fromDate, DateTime toDate) {
        return createReport(Restrictions.eq("companyId",parkingSubLot.getId()),fromDate,toDate);
    }

    public ParkingReport createReport(Criterion fetchCriteria, DateTime fromDate, DateTime toDate) {

        List list = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_IN"))
                .setProjection(Projections.rowCount()).list();

        Integer checkInCount = 0;
        if (list != null) checkInCount = ((Long)list.get(0)).intValue();

        list = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .setProjection(Projections.rowCount()).list();

        Integer checkOutCount = 0;
        if (list != null) checkOutCount = ((Long)list.get(0)).intValue();

        list = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("eventType", "CHECKED_IN")).list();

        BigDecimal checkInRevenue = (BigDecimal) list.get(0);
        if (checkInRevenue == null) checkInRevenue = new BigDecimal(0);

        list = currentSession().createCriteria(ParkingEventEntity.class)
                .add(fetchCriteria)
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("eventType", "CHECKED_OUT")).list();

        BigDecimal checkOutRevenue = (BigDecimal) list.get(0);
        if (checkOutRevenue == null) checkOutRevenue = new BigDecimal(0);

        return new ParkingReport(checkInCount,checkOutCount,checkInRevenue,checkOutRevenue);

    }
}
