package com.getMyParking.dao;

import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by rahulgupta.s on 02/10/15.
 */
public class ParkingSubLotUserAccessDAO extends AbstractDAO<ParkingSubLotUserAccessEntity> {

    @Inject
    public ParkingSubLotUserAccessDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToParking(Integer parkingId) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.eq("parkingId", parkingId)))));
    }

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToParkingLot(Integer parkingLotId) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.eq("parkingLotId", parkingLotId)))));
    }

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToParkingSubLot(Integer parkingSubLotId) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.eq("parkingSubLotId", parkingSubLotId)))));
    }

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToParkingSubLots(List<Integer> parkingSublotIds) {
        return list(criteria().add(Restrictions.in("parkingSubLotId", parkingSublotIds)));
    }

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToCompany(Integer companyId) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.eq("companyId", companyId)))));
    }

    public void deleteByParkingId(Integer parkingId){
        Query q = currentSession().createQuery("delete from ParkingSubLotUserAccessEntity where parking_id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

    public void deleteByParkingLotId(Integer parkingLotId){
        Query q = currentSession().createQuery("delete from ParkingSubLotUserAccessEntity where parking_lot_id =:id");
        q.setInteger("id", parkingLotId);
        q.executeUpdate();
    }

    public void deleteByParkingSubLotId(Integer parkingSubLotId){
        Query q = currentSession().createQuery("delete from ParkingSubLotUserAccessEntity where parking_sub_lot_id =:id");
        q.setInteger("id", parkingSubLotId);
        q.executeUpdate();
    }

}
