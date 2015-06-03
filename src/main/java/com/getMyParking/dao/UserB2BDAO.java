package com.getMyParking.dao;

import com.getMyParking.entity.UserB2BEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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


    public UserB2BEntity loginUser(String userName, String password) {
        Criteria criteria = currentSession().createCriteria(UserB2BEntity.class);
        criteria.add(Restrictions.eq("username",userName));
        criteria.add(Restrictions.eq("password",password));
        return uniqueResult(criteria);
    }

    public void saveUser(UserB2BEntity user) {
        UserB2BEntity userEntity = get(user.getUsername());
        if (userEntity == null)
            persist(user);
        else
            throw new WebApplicationException(Response.status(401).entity("Username already exists").build());
    }

    public UserB2BEntity findById(String username) {
        return get(username);
    }
}
