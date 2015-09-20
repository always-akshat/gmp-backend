package com.getMyParking.dao;

import com.getMyParking.entity.ParkingPassEntity;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by rahulgupta.s on 01/06/15.
 */
public class ParkingPassDAO extends AbstractDAO<ParkingPassEntity> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public ParkingPassDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public void saveOrUpdateParkingPass(ParkingPassEntity parkingPass) {
        persist(parkingPass);
    }

    public ParkingPassEntity findById(Integer passId) {
        return get(passId);
    }

    public List<ParkingPassEntity> findByIds(List<String> parkingPassIds) {
        List<Integer> parkingPassIdInts = Lists.transform(parkingPassIds, new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        });
        return list(criteria().add(Restrictions.in("parkingPassMaster.id",parkingPassIdInts))
                              .add(Restrictions.gt("validTime", DateTime.now())));
    }

    public List<ParkingPassEntity> findByPassIds(List<String> parkingPassIds) {
        List<Integer> parkingPassIdInts = Lists.transform(parkingPassIds, new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        });
        return list(criteria().add(Restrictions.in("id",parkingPassIdInts)));
    }
}
