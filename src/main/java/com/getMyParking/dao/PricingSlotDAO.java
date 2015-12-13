package com.getMyParking.dao;

import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import java.util.List;

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
    public PricingSlotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.priceGridDAO = new PriceGridDAO(sessionFactory);
    }

    public PricingSlotEntity findById(Integer pricingSlotId) {
        return get(pricingSlotId);
    }

    public void saveOrUpdatePricingSlot(PricingSlotEntity priceSlot) {
        if(priceSlot.getId()==null)
            persist(priceSlot);
        else
            currentSession().merge(priceSlot);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from PricingSlotEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

    public List<PricingSlotEntity> findBySubLotId(int id) {
        SQLQuery q = currentSession().createSQLQuery("SELECT * from pricing_slot where pricing_slot.parking_sub_lot_id =:id");
        q.setInteger("id", id);
        q.addEntity(PricingSlotEntity.class);
        return q.list();
    }
}
