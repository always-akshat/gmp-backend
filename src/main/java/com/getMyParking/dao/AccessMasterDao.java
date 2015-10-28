package com.getMyParking.dao;

import com.getMyParking.entity.AccessMasterEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by karan on 13/10/15.
 */
public class AccessMasterDao extends AbstractDAO<AccessMasterEntity> {

    @Inject
    public AccessMasterDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<AccessMasterEntity> findAll() {
        Query q = currentSession().createQuery("from AccessMasterEntity");
        return list(q);
    }



}
