package com.getMyParking.dao;

import com.getMyParking.entity.PricingSlotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class PricingSlotDAO extends AbstractDAO<PricingSlotEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public PricingSlotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public PricingSlotEntity findById(Integer pricingSlotId) {
        return get(pricingSlotId);
    }

    public void saveOrUpdatePricingSlot(PricingSlotEntity priceSlot) {
        persist(priceSlot);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from PricingSlotEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

}
