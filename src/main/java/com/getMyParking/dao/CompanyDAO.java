package com.getMyParking.dao;

import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.ParkingEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class CompanyDAO extends AbstractDAO<CompanyEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public CompanyDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateCompany(CompanyEntity company) {
        persist(company);
    }


    public CompanyEntity findById(Integer companyId) {
        return get(companyId);
    }

    public void deleteById(Integer companyId) {
        Query q = currentSession().createQuery("delete from CompanyEntity where id =:id");
        q.setInteger("id", companyId);
        q.executeUpdate();
    }

    public List<CompanyEntity> findByName(String name){
        return list(
                criteria().add(
                        Restrictions.eq("name", name)
                ));
    }
}
