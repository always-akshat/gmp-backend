package com.getMyParking.dao;

import com.getMyParking.dto.ActiveParkingPassDTO;
import com.getMyParking.entity.ParkingPassEntity;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.jersey.params.IntParam;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
                .add(Restrictions.gt("validTime", DateTime.now()))
                .add(Restrictions.ne("isDeleted", 1)));
    }

    public ParkingPassEntity getLatestPass(String registrationNumber, Integer parkingPassMasterId) {

        return uniqueResult(
                criteria().add(Restrictions.eq("parkingPassMaster.id", parkingPassMasterId))
                        .add(Restrictions.eq("registrationNumber", registrationNumber))
                        .add(Restrictions.ne("isDeleted", 1))
                        .addOrder(Order.desc("validTime"))
                        .setMaxResults(1)
        );
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

    public List<ParkingPassEntity> searchParkingPass(Optional<IntParam> parkingId, Optional<String> registrationNumber,
                                                     Optional<IntParam> isDeleted,
                                                     Integer integer, Integer pageSize) {
        Criteria criteria = criteria();

        if (parkingId.isPresent()) {
            criteria.add(Restrictions.eq("parkingPassMaster.parkingId",parkingId.get().get()));
        }

        if (registrationNumber.isPresent()) {
            criteria.add(Restrictions.eq("registrationNumber",registrationNumber.get()));
        }

        if (isDeleted.isPresent()) {
            criteria.add(Restrictions.eq("isDeleted",isDeleted.get()));
        }

        criteria.setFirstResult((integer-1)*pageSize);
        criteria.setMaxResults(pageSize);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return list(criteria);
    }
}
