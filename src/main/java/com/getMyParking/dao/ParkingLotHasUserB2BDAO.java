package com.getMyParking.dao;

import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by rahulgupta.s on 04/06/15.
 */
public class ParkingLotHasUserB2BDAO extends AbstractDAO<ParkingSubLotUserAccessEntity>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingLotHasUserB2BDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public void saveUserAccess(List<ParkingSubLotUserAccessEntity> parkingSubLotUserAccessList) {
        for (ParkingSubLotUserAccessEntity parkingSubLotUserAccess : parkingSubLotUserAccessList) {
            persist(parkingSubLotUserAccess);
        }
    }
}
