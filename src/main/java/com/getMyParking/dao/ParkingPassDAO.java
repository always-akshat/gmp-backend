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
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;

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

        SQLQuery query = currentSession().createSQLQuery("SELECT `parking_pass`.*, Count(*) as count,  SUM(`parking_pass`.`is_paid`) as isPaidCount " +
                "from `parking_pass` inner join `parking_pass_master` on `parking_pass`.`parking_pass_master_id` = `parking_pass_master`.`id` " +
                "where `parking_pass`.`valid_time` >= CURRENT_TIMESTAMP and `parking_pass`.`parking_pass_master_id` IN" +
                " :parkingPassIds and `parking_pass`.`is_deleted` = 0 group by `parking_pass`.`registration_number`,`parking_pass`.`parking_pass_master_id`");
        query.setParameterList("parkingPassIds",parkingPassIdInts);
        query.addEntity("parking_pass",ParkingPassEntity.class);
        query.addScalar("count", IntegerType.INSTANCE);
        query.addScalar("isPaidCount", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ActiveParkingPassDTO.class));

        List<ActiveParkingPassDTO> list = query.list();

        return Lists.transform(list, new Function<ActiveParkingPassDTO, ParkingPassEntity>() {
            @Nullable
            @Override
            public ParkingPassEntity apply(ActiveParkingPassDTO activeParkingPassDTO) {
                ParkingPassEntity entity = activeParkingPassDTO.getParkingPass();
                entity.setBalanceAmount(
                        entity.getParkingPassMaster().getPrice() * (activeParkingPassDTO.getCount() - activeParkingPassDTO.getIsPaidCount())
                );
                return entity;
            }
        });
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
