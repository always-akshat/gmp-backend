package com.getMyParking.dao;

import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
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

    public List<ParkingSubLotUserAccessEntity> getAllUsersWithAccessToCompany(Integer companyId) {
        return Lists.newArrayList(Sets.newHashSet(list(currentSession().createCriteria(ParkingSubLotUserAccessEntity.class)
                .add(Restrictions.eq("companyId", companyId)))));
    }

}