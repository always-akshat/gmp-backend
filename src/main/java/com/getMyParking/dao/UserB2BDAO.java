package com.getMyParking.dao;

import com.getMyParking.entity.UserB2BEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class UserB2BDAO extends AbstractDAO<UserB2BEntity> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public UserB2BDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


}
