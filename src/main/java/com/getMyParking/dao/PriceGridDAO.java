package com.getMyParking.dao;

import com.getMyParking.entity.PriceGridEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class PriceGridDAO extends AbstractDAO<PriceGridEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public PriceGridDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public PriceGridEntity findById(Integer priceGridId) {
        return get(priceGridId);
    }

    public void saveOrUpdatePricingSlot(PriceGridEntity priceGrid) {
        if(priceGrid.getId()==null)
            persist(priceGrid);
        else
           currentSession().merge(priceGrid);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from PriceGridEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

}
