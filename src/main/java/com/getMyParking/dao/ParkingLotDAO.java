package com.getMyParking.dao;

import com.getMyParking.entities.ParkingLot;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by rahulgupta.s on 15/03/15.
 */
public class ParkingLotDAO extends AbstractDAO<ParkingLot> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public ParkingLotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveParkingLot(ParkingLot parkingLot) {
        persist(parkingLot);
    }

    public ParkingLot getParkingLot(Integer parkingLotId) {
        return (ParkingLot) currentSession().get(ParkingLot.class,parkingLotId);
    }
}
