package com.getMyParking.dao;

import com.getMyParking.entity.ParkingPassEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rahulgupta.s on 01/06/15.
 */
public class ParkingPassDAO extends AbstractDAO<ParkingPassEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingPassDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public void saveOrUpdateParkingLot(ParkingPassEntity parkingPass) {
        persist(parkingPass);
    }

    public ParkingPassEntity findById(Integer passId) {
        return get(passId);
    }

    public List<ParkingPassEntity> findByIds(List<String> parkingPassIds) {
        return list(criteria().add(Restrictions.in("parkingPassMaster.id",parkingPassIds))
                              .add(Restrictions.gt("validTime", DateTime.now())));
    }
}
