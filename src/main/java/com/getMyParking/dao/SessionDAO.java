package com.getMyParking.dao;

import com.getMyParking.entity.SessionEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class SessionDAO extends AbstractDAO<SessionEntity>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public SessionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public void saveSession(SessionEntity sessionEntity) {
        persist(sessionEntity);
    }

    public SessionEntity findByAuthToken(String authToken) {
        return uniqueResult(criteria().add(Restrictions.eq("authToken",authToken)));
    }

    public SessionEntity findActiveSession(String userName) {
        return uniqueResult(criteria().add(Restrictions.eq("userB2BEntity.username", userName))
                .addOrder(Order.desc("validTime")));
    }
}
