package com.getMyParking.dao;

import com.getMyParking.entity.PriceGridEntity;
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

    private PriceGridDAO priceGridDAO;

    @Inject
    public PricingSlotDAO(SessionFactory sessionFactory,PriceGridDAO priceGridDAO) {
        super(sessionFactory);
        this.priceGridDAO = priceGridDAO;
    }

    public PricingSlotEntity findById(Integer pricingSlotId) {
        return get(pricingSlotId);
    }

    public void saveOrUpdatePricingSlot(PricingSlotEntity priceSlot) {
        if(priceSlot.getId()==null)
            persist(priceSlot);
        else
            currentSession().merge(priceSlot);
        if(priceSlot.getPriceGrids()!=null)
            for(PriceGridEntity priceGridEntity : priceSlot.getPriceGrids()){
                priceGridEntity.setPricingSlot(priceSlot);
                priceGridDAO.saveOrUpdatePricingSlot(priceGridEntity);
            }
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from PricingSlotEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

}
