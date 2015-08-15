package com.getMyParking.dao;

import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 15/08/15.
 */
public class ParkingSubLotDAO extends AbstractDAO<ParkingSubLotEntity> {

    @Inject
    public ParkingSubLotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public ParkingSubLotEntity findById(Integer id) {
        return get(id);
    }

    public void saveOrUpdateParkingLot(ParkingSubLotEntity parkingSubLot) {
        persist(parkingSubLot);
    }
}
