package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingDAO extends AbstractDAO<ParkingEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateParking(ParkingEntity parking) {
        if(parking.getId()==null)
            persist(parking);
        else if(parking.getId()==0){
            persist(parking);
        }else
            currentSession().merge(parking);
    }


    public ParkingEntity findById(Integer parkingId) {
        return get(parkingId);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from ParkingEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

    public List<ParkingEntity> findByName(String name){
        return list(
                criteria().add(
                        Restrictions.like("name", name, MatchMode.ANYWHERE)
                ));
    }
}
