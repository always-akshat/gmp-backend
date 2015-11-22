package com.getMyParking.dao;

import com.getMyParking.entity.ParkingPassMasterEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        if(parkingPass.getId()==null)
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

    public List<ParkingPassMasterEntity> getPassMasters(Integer parkingId) {
        return criteria().add(Restrictions.eq("parking.id", parkingId))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }
    public void deleteParkingPassMasterByParkingId(Integer id){
        Query q = currentSession().createQuery("delete from ParkingPassMasterEntity where parking_id =:id");
        q.setInteger("id", id);
        q.executeUpdate();
    }
}
