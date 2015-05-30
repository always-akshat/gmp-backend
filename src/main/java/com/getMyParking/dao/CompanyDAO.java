package com.getMyParking.dao;

import com.getMyParking.entity.Company;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class CompanyDAO extends AbstractDAO<Company> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public CompanyDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdateCompany(Company company) {
        persist(company);
    }


    public Company findById(Integer companyId) {
        return findById(companyId);
    }

    public void deleteById(Integer companyId) {
        Query q = currentSession().createQuery("delete from Company where id =:id");
        q.setInteger("id", companyId);
        q.executeUpdate();
    }
}
