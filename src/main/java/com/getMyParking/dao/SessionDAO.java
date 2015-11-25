package com.getMyParking.dao;

import com.getMyParking.dto.ActiveSessions;
import com.getMyParking.entity.SessionEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CustomType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;

import java.util.List;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class SessionDAO extends AbstractDAO<SessionEntity>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public SessionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public void saveSession(SessionEntity sessionEntity) {
        persist(sessionEntity);
    }

    public void updateSession(SessionEntity sessionEntity) {
        currentSession().merge(sessionEntity);
    }

    public SessionEntity findByAuthToken(String authToken) {
        return uniqueResult(criteria().add(Restrictions.eq("authToken",authToken)));
    }

    public SessionEntity findActiveSession(String userName) {
        return uniqueResult(criteria().add(Restrictions.eq("userB2BEntity.username", userName))
                .addOrder(Order.desc("validTime")));
    }

    public List<ActiveSessions> getActiveSessions() {
        SQLQuery sqlQuery = currentSession().createSQLQuery("select s.username as username, user.name as name , s.last_transaction_time as lastTransactionTime, s.last_access_time as lastAccessTime," +
                " s.device_id as deviceId, p.name as parkingLotName , s.transaction_count as transactionCount " +
                "from session as s inner join parking_lot p on p.id = s.last_accessed_parking_lot_id inner join" +
                " user_b2b as user on user.username = s.username group by s.username order by s.last_access_time;");

        sqlQuery.addScalar("username",StringType.INSTANCE);
        sqlQuery.addScalar("name",StringType.INSTANCE);
        sqlQuery.addScalar("deviceId",StringType.INSTANCE);
        sqlQuery.addScalar("parkingLotName",StringType.INSTANCE);
        sqlQuery.addScalar("lastAccessTime", new CustomType(new PersistentDateTime()));
        sqlQuery.addScalar("lastTransactionTime", new CustomType(new PersistentDateTime()));
        sqlQuery.addScalar("transactionCount", IntegerType.INSTANCE);
        return sqlQuery.list();

    }
}
