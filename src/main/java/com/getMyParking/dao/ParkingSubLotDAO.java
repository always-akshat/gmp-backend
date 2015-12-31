package com.getMyParking.dao;

import com.getMyParking.entity.ParkingSubLotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
        if(parkingSubLot.getId()==null)
            persist(parkingSubLot);
        else
            currentSession().merge(parkingSubLot);
    }

    public List<ParkingSubLotEntity> getAllAutoCheckoutParkingLots(){
        return list(
                criteria()
                .add(Restrictions.neOrIsNotNull("autoCheckoutTime", null))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
        );
    }

    public ParkingSubLotEntity getSubLotBy(String vehicleType, List<Integer> parkingSubLotIds) {
        return uniqueResult(
                criteria().add(Restrictions.in("id",parkingSubLotIds))
                .add(Restrictions.eq("type",vehicleType))
        );
    }

    public List<ParkingSubLotEntity> findByIds(List<Integer> parkingSubLotIds) {
        return list(
                criteria().add(Restrictions.in("id", parkingSubLotIds)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
        );
    }
}
