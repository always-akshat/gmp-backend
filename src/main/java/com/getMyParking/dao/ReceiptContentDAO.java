package com.getMyParking.dao;

import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.entity.ReceiptContentEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class ReceiptContentDAO extends AbstractDAO<ReceiptContentEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ReceiptContentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ReceiptContentEntity findById(Integer id) {
        return get(id);
    }

    public void saveOrUpdatePricingSlot(ReceiptContentEntity receiptContent) {
        if(receiptContent.getId()==null)
            persist(receiptContent);
        else
            currentSession().merge(receiptContent);
    }

    public void deleteById(Integer parkingId) {
        Query q = currentSession().createQuery("delete from ReceiptContentEntity where id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }
}
