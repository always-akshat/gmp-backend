package com.getMyParking.task;

import com.getMyParking.dao.SessionDAO;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by rahulgupta.s on 25/11/15.
 */
public class SessionSaveTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SessionSaveTask.class);

    private SessionFactory sessionFactory;
    private SessionDAO sessionDAO;
    private LoadingCache<String,GMPUser> sessionCache;

    @Inject
    public SessionSaveTask(SessionDAO sessionDAO, @Named("authTokenCache") LoadingCache<String,GMPUser> sessionCache,
                           SessionFactory sessionFactory) {
        this.sessionDAO = sessionDAO;
        this.sessionCache = sessionCache;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        Transaction transaction = session.beginTransaction();
        try {
            ConcurrentMap<String,GMPUser> contents = sessionCache.asMap();
            contents.forEach((s, gmpUser) -> {
                sessionDAO.updateSession(gmpUser.getSession());
            });
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception ex) {
            logger.error("Session Save Task Exception ", ex);
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }

    }

}
