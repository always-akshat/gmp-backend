package com.getMyParking.dao;

import com.getMyParking.entities.Parking;
import com.getMyParking.entities.ParkingLot;
import com.getMyParking.entities.ParkingReport;
import com.getMyParking.entities.UpdateParkingRequest;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rahulgupta.s on 14/03/15.
 */
public class ParkingDAO extends AbstractDAO<Parking> {

    public ParkingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<Parking> getParkingByParkingLotId(Integer id) {
        Criteria criteria = currentSession().createCriteria(Parking.class);
        Criterion criterion = Restrictions.eq("parkingLot.id", id);
        criteria.add(criterion);
        criteria.addOrder(Order.desc("checkInTime"));
        return list(criteria);
    }

    public void saveParking(List<Parking> parkingList, Integer parkingLotId) {
        for (Parking parking : parkingList) {
            Parking p = (Parking) currentSession().createCriteria(Parking.class)
                    .add(Restrictions.eq("parkingSerialNumber",parking.getParkingSerialNumber())).uniqueResult();
            if (p == null) {
                persist(parking);
            } else {
                p.copy(parking);
                persist(p);
            }
        }
    }

    public ParkingReport createReport(Integer parkingLotId, DateTime fromDate, DateTime toDate) {

        List cars = currentSession().createCriteria(Parking.class)
                .add(Restrictions.eq("parkingLotId",parkingLotId))
                .add(Restrictions.between("checkInTime", fromDate, toDate))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("type", "CAR")).list();

        Integer carNumbers = (Integer) cars.get(0);

        List bikes = currentSession().createCriteria(Parking.class)
                .add(Restrictions.eq("parkingLotId", parkingLotId))
                .add(Restrictions.between("checkInTime", fromDate, toDate))
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("type", "BIKE")).list();

        Integer bikeNumbers = (Integer) bikes.get(0);

        List carsRevenue = currentSession().createCriteria(Parking.class)
                .add(Restrictions.eq("parkingLotId", parkingLotId))
                .add(Restrictions.between("checkInTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("type", "CAR")).list();

        Integer carsTotal = (Integer) carsRevenue.get(0);

        List bikesRevenue = currentSession().createCriteria(Parking.class)
                .add(Restrictions.eq("parkingLotId", parkingLotId))
                .add(Restrictions.between("checkInTime", fromDate, toDate))
                .setProjection(Projections.sum("cost"))
                .add(Restrictions.eq("type", "BIKE")).list();

        Integer bikeTotal = (Integer) bikesRevenue.get(0);

        return new ParkingReport(carNumbers,bikeNumbers,carsTotal,bikeTotal);



    }
}
