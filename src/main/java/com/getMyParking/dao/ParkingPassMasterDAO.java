package com.getMyParking.dao;

import com.getMyParking.entity.ParkingPassMasterEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 01/06/15.
 */
public class ParkingPassMasterDAO extends AbstractDAO<ParkingPassMasterEntity>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingPassMasterDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateParking(ParkingPassMasterEntity parkingPass) {
        persist(parkingPass);
    }


    public ParkingPassMasterEntity findById(Integer id) {
        return get(id);
    }


}
