package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEventEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ParkingEventDAO extends AbstractDAO<ParkingEventEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingEventDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateParkingEvent(ParkingEventEntity event) {
        persist(event);
    }


    public ParkingEventEntity findById(Integer id) {
        return get(id);
    }

    public List<ParkingEventEntity> getParkingEvents(int parkingLotId, DateTime lastUpdateTime) {
        Query q = currentSession().createQuery("from ParkingEventEntity where parkingLotByParkingLotId.id =:id and updatedTime >= :updatedTime");
        q.setInteger("id", parkingLotId);
        q.setString("updatedTime",lastUpdateTime.toString());
        return list(q);
    }
}
