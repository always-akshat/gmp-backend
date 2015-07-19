package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingLotDAO extends AbstractDAO<ParkingLotEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingLotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateParkingLot(ParkingLotEntity parkingLot) {
        persist(parkingLot);
    }


    public ParkingLotEntity findById(Integer parkingLotId) {
        return get(parkingLotId);
    }

    public void deleteById(Integer companyId) {
        Query q = currentSession().createQuery("delete from ParkingLotEntity where id =:id");
        q.setInteger("id", companyId);
        q.executeUpdate();
    }

    public List<ParkingLotEntity> getAutoCheckoutParkingLotList() {
        return list(currentSession().createCriteria(ParkingEventEntity.class)
                .add(Restrictions.isNotNull("autoCheckoutTime")));
    }
}
