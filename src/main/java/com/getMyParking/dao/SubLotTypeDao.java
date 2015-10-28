package com.getMyParking.dao;

import com.getMyParking.entity.SubLotTypeEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by karan on 14/10/15.
 */
public class SubLotTypeDao extends AbstractDAO<SubLotTypeEntity> {

    @Inject
    public SubLotTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<SubLotTypeEntity> findAll(){
        Query q = currentSession().createQuery("from SubLotTypeEntity");
        return list(q);
    }
}
