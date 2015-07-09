package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingReport;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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

    public List<ParkingEventEntity> getParkingEvents(int parkingLotId, DateTime lastUpdateTime) {
        Query q = currentSession().createQuery("from ParkingEventEntity where parkingLotByParkingLotId.id =:id and updatedTime >= :updatedTime");
        q.setInteger("id", parkingLotId);
        q.setString("updatedTime",lastUpdateTime.toString());
        return list(q);
    }

    public ParkingEventEntity findBySerialNumberAndEventType(String eventType, String serialNumber) {
        Criteria criteria = currentSession().createCriteria(ParkingEventEntity.class);
        criteria.add(Restrictions.eq("eventType",eventType))
                .add(Restrictions.eq("serialNumber",serialNumber));

        return uniqueResult(criteria);
    }

    public ParkingReport createReport(Integer parkingLotId, DateTime fromDate, DateTime toDate) {

        List cars = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id",parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_OUT"))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("vehicleType", "CAR")).list();

        Long carNumbers = (Long) cars.get(0);
        if (carNumbers == null) carNumbers = 0L;

        List zeroCost = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id",parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_OUT"))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("vehicleType", "CAR"))
                .add(Restrictions.eq("cost",0.0)).list();

        Long zeroCostNumber = (Long) zeroCost.get(0);
        if (zeroCostNumber == null) zeroCostNumber = 0L;

        carNumbers = carNumbers - zeroCostNumber;

        List bikes = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id", parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType", "CHECKED_OUT"))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("vehicleType", "BIKE")).list();

        Long bikeNumbers = (Long) bikes.get(0);
        if (bikeNumbers == null) bikeNumbers = 0L;

        zeroCost = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id",parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .add(Restrictions.eq("eventType","CHECKED_OUT"))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("vehicleType", "BIKE"))
                .add(Restrictions.eq("cost",0.0)).list();

        zeroCostNumber = (Long) zeroCost.get(0);
        if (zeroCostNumber == null) zeroCostNumber = 0L;

        bikeNumbers = bikeNumbers - zeroCostNumber;

        List carsRevenue = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id", parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("vehicleType", "CAR")).list();

        BigDecimal carsTotal = (BigDecimal) carsRevenue.get(0);
        if (carsTotal == null) carsTotal = new BigDecimal(0);

        List bikesRevenue = currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.eq("parkingLotByParkingLotId.id", parkingLotId))
                .add(Restrictions.between("eventTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("vehicleType", "BIKE")).list();

        BigDecimal bikeTotal = (BigDecimal) bikesRevenue.get(0);
        if (bikeTotal == null) bikeTotal = new BigDecimal(0);

        return new ParkingReport(carNumbers.intValue(),bikeNumbers.intValue(),carsTotal.intValue(),bikeTotal.intValue());



    }
}
