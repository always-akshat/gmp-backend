package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEntity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingDAO extends AbstractDAO<ParkingEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public ParkingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public void saveOrUpdateParking(ParkingEntity parking) {
        persist(parking);
    }


    public ParkingEntity findById(Integer parkingId) {
        return get(parkingId);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from ParkingEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }
}
