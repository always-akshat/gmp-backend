package com.getMyParking.dao;

import com.getMyParking.entity.ParkingLotHasUserB2BEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by rahulgupta.s on 04/06/15.
 */
public class ParkingLotHasUserB2BDAO extends AbstractDAO<ParkingLotHasUserB2BEntity>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingLotHasUserB2BDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public void saveParkingLotId(List<Integer> parkingSubLotIds, UserB2BEntity user) {
        for (Integer parkingSubLotId : parkingSubLotIds) {
            ParkingLotHasUserB2BEntity entity = new ParkingLotHasUserB2BEntity(parkingSubLotId,user);
            persist(entity);
        }
    }
}
