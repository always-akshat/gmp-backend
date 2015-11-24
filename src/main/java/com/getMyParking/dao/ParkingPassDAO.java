package com.getMyParking.dao;

import com.getMyParking.dto.ActiveParkingPassDTO;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.getMyParking.entity.reports.PassReport;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.jersey.params.IntParam;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        /*SQLQuery query = currentSession().createSQLQuery("SELECT `parking_pass`.*, Count(*) as count,  SUM(`parking_pass`.`is_paid`) as isPaidCount " +
                "from `parking_pass` inner join `parking_pass_master` on `parking_pass`.`parking_pass_master_id` = `parking_pass_master`.`id` " +
                "where `parking_pass`.`valid_time` >= CURRENT_TIMESTAMP and `parking_pass`.`parking_pass_master_id` IN" +
                " :parkingPassIds and `parking_pass`.`is_deleted` = 0 group by `parking_pass`.`registration_number`,`parking_pass`.`parking_pass_master_id`");*/
        SQLQuery query = currentSession().createSQLQuery("SELECT `parking_pass`.*, Count(*) as count,  SUM(`parking_pass`.`is_paid`) as isPaidCount " +
                "from `parking_pass` inner join `parking_pass_master` on `parking_pass`.`parking_pass_master_id` = `parking_pass_master`.`id` " +
                "where `parking_pass`.`valid_time` >= CURRENT_TIMESTAMP and `parking_pass`.`parking_pass_master_id` IN" +
                " :parkingPassIds group by `parking_pass`.`registration_number`,`parking_pass`.`parking_pass_master_id`");
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

    public List<PassReport> passReport(List<ParkingPassMasterEntity> parkingPassMasters) {

        List<Integer> parkingPassMasterIds =
                parkingPassMasters.stream().map(ParkingPassMasterEntity::getId).collect(Collectors.toList());
        Map<Integer,Integer> masterPrice = parkingPassMasters.stream()
                .collect(Collectors.toMap(ParkingPassMasterEntity::getId, ParkingPassMasterEntity::getPrice));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.rowCount());

        projectionList.add(Projections.groupProperty("parkingPassMaster.id"));
        Criteria criteria = criteria().add(Restrictions.gt("validTime", DateTime.now()))
                                      .add(Restrictions.eq("isDeleted", 0))
                                      .add(Restrictions.in("parkingPassMaster.id", parkingPassMasterIds))
                                      .setProjection(projectionList);

        List<Object[]> result = criteria.list();
        Map<Integer,PassReport> passReportMap = Maps.newHashMap();
        result.stream().forEach(objects -> {
            PassReport passReport = new PassReport();
            passReport.setActivePassCount((Long) objects[0]);
            passReport.setParkingPassMasterId((Integer) objects[1]);
            passReportMap.put(passReport.getParkingPassMasterId(),passReport);
        });

        DateTime startTime = DateTime.now().toDateTime(DateTimeZone.forOffsetHoursMinutes(5, 30))
                .dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
        DateTime endTime = startTime.plusMonths(1).minusSeconds(1);

        result = criteria().add(Restrictions.between("validFrom", startTime, endTime))
                .add(Restrictions.eq("isPaid", 1))
                .add(Restrictions.in("parkingPassMaster.id", parkingPassMasterIds))
                .setProjection(projectionList).list();

        result.stream().forEach(objects -> {
            PassReport passReport = passReportMap.get(objects[1]);
            passReport.setPaidPassCount((Long) objects[0]);
            passReport.setCollectedAmount(new BigDecimal((Long) objects[0] * masterPrice.get(objects[1])));
        });

        result = criteria().add(Restrictions.eq("isPaid", 0))
                .add(Restrictions.in("parkingPassMaster.id", parkingPassMasterIds))
                .setProjection(projectionList).list();

        result.stream().forEach(objects -> {
            PassReport passReport = passReportMap.get(objects[1]);
            passReport.setBalanceAmount(new BigDecimal((Long) objects[0] * masterPrice.get(objects[1])));
        });

        return passReportMap.values().stream().collect(Collectors.toList());
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

    public List<ParkingPassEntity> searchParkingPass(Integer parkingId, String registrationNumber,
                                                     Optional<IntParam> isDeleted,
                                                     Integer integer, Integer pageSize) {

        Query q;
        if (isDeleted.isPresent()) {
            q = currentSession().createQuery(" FROM ParkingPassEntity WHERE parkingPassMaster.parking.id = :parkingId " +
                    "AND registrationNumber = :registrationNumber AND isDeleted = :isDeleted");
            q.setInteger("isDeleted",isDeleted.get().get());
        } else {
            q = currentSession().createQuery(" FROM ParkingPassEntity WHERE parkingPassMaster.parking.id = :parkingId " +
                    "AND registrationNumber = :registrationNumber");
        }

        q.setInteger("parkingId",parkingId);
        q.setString("registrationNumber", registrationNumber);

        q.setFirstResult((integer-1) * pageSize);
        q.setMaxResults(pageSize);
        q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return q.list();
    }

    public void deleteByMasterId(Integer id){
        Query q = currentSession().createQuery("delete from ParkingPassEntity where parkingPassMaster.id =:id");
        q.setInteger("id", id);
        q.executeUpdate();
    }

    public List<ParkingPassEntity> getAllPassesWithRegistrationNumberAndParkingId(String registrationNumber, Integer parkingId) {

        Query q = currentSession().createQuery(" FROM ParkingPassEntity WHERE parkingPassMaster.parking.id = :parkingId " +
                "AND registrationNumber = :registrationNumber");
        q.setInteger("parkingId",parkingId);
        q.setString("registrationNumber", registrationNumber);
        q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return q.list();
    }
}
