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

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToSubLots(List<Integer> subLotIds) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.in("parkingSubLotId", subLotIds)))));
    }

    public void deleteByParkingId(Integer parkingId){
        Query q = currentSession().createQuery("delete from ParkingSubLotUserAccessEntity where parking_id =:id");
        q.setInteger("id", parkingId);
        q.executeUpdate();
    }

}
