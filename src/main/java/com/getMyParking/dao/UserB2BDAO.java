package com.getMyParking.dao;

import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.getMyParking.service.auth.GMPAdmin;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

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
        if(!password.equals(GMPAdmin._MASTERPASSWORD))
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

    public void editUser(UserB2BEntity user) {
        currentSession().merge(user);
    }


    public UserB2BEntity findById(String username) {
        return get(username);
    }

    public void updateUser(UserB2BEntity userB2BEntity) {
        currentSession().update(userB2BEntity);
    }

    public List<UserB2BEntity> findByName(String name){
        return list(
                criteria().add(
                        Restrictions.like("name", name, MatchMode.ANYWHERE)
                ));
    }
}
