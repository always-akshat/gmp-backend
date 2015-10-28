package com.getMyParking.dao;

import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by rahulgupta.s on 15/08/15.
 */
public class ParkingSubLotDAO extends AbstractDAO<ParkingSubLotEntity> {

    private PricingSlotDAO pricingSlotDAO;

    @Inject
    public ParkingSubLotDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.pricingSlotDAO = new PricingSlotDAO(sessionFactory);
    }


    public ParkingSubLotEntity findById(Integer id) {
        return get(id);
    }

    public void saveOrUpdateParkingLot(ParkingSubLotEntity parkingSubLot) {
//        if(parkingSubLot.getId()==null)
            persist(parkingSubLot);
//        else
//            currentSession().merge(parkingSubLot);
        System.out.println(parkingSubLot.getCapacity()+" "+parkingSubLot.getPricingSlots().size()+" ");
        if(parkingSubLot.getPricingSlots()!=null)
            for(PricingSlotEntity pricingSlotEntity : parkingSubLot.getPricingSlots()){
                System.out.println("here");
                pricingSlotEntity.setParkingSubLot(parkingSubLot);
                pricingSlotDAO.saveOrUpdatePricingSlot(pricingSlotEntity);
            }

    }

    public void deleteParkingSublotById(Integer id){
        Query q = currentSession().createQuery("delete from ParkingSubLotEntity where id =:id");
        q.setInteger("id", id);
        q.executeUpdate();
    }

    public List<ParkingSubLotEntity> getAllAutoCheckoutParkingLots(){
        return list(
                criteria()
                .add(Restrictions.neOrIsNotNull("autoCheckoutTime",null))
        );
    }
}
