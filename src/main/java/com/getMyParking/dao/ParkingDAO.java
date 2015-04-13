package com.getMyParking.dao;

import com.getMyParking.entities.Parking;
import com.getMyParking.entities.ParkingLot;
import com.getMyParking.entities.UpdateParkingRequest;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
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
            Parking p = (Parking) currentSession().get(Parking.class, parking.getId());
            if (p == null) {
                parking.setParkingLotId(parkingLotId);
                persist(parking);
            } else {
                p.copy(parking);
                persist(p);
            }
        }
    }

}
