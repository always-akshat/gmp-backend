package com.getMyParking.dao;

import com.getMyParking.entity.ParkingPassMasterEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
        if(parkingPass.getId()==null || (parkingPass.getId()!=null && parkingPass.getId()==0))
            persist(parkingPass);
        else
            currentSession().merge(parkingPass);
    }


    public ParkingPassMasterEntity findById(Integer id) {
        return get(id);
    }

    public List<ParkingPassMasterEntity> findByParkingId(Integer id){
        return list(
                criteria().add(
                        Restrictions.eq("parking.id", id)
                ));
    }

    public void deleteParkingPassMasterByParkingId(Integer id){
        Query q = currentSession().createQuery("delete from ParkingPassMasterEntity where parking_id =:id");
        q.setInteger("id", id);
        q.executeUpdate();
    }
}
