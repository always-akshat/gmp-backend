package com.getMyParking.dao;

import com.getMyParking.entity.UserAccessEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by karan on 13/10/15.
 */
public class UserAccessDao extends AbstractDAO<UserAccessEntity> {

    @Inject
    public UserAccessDao(SessionFactory sessionFactory){
        super(sessionFactory);
    }

    public void saveUserAccess(UserAccessEntity userAccessEntity){
        persist(userAccessEntity);
//        currentSession().merge(userAccessEntity);
    }

}
