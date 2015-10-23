package com.getMyParking.dao;

import com.getMyParking.entity.StyleMasterEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by karan on 19/10/15.
 */
public class StyleMasterDao extends AbstractDAO<StyleMasterEntity>{

    @Inject
    public StyleMasterDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<StyleMasterEntity> findAll(){
        Query q = currentSession().createQuery("from StyleMasterEntity");
        return list(q);
    }
}
