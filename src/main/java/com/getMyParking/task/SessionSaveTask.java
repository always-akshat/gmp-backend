package com.getMyParking.task;

import com.getMyParking.dao.SessionDAO;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by rahulgupta.s on 25/11/15.
 */
public class SessionSaveTask implements Runnable {

    private SessionDAO sessionDAO;
    private LoadingCache<String,GMPUser> sessionCache;

    @Inject
    public SessionSaveTask(SessionDAO sessionDAO, @Named("authTokenCache") LoadingCache<String,GMPUser> sessionCache) {
        this.sessionDAO = sessionDAO;
        this.sessionCache = sessionCache;
    }

    @Override
    public void run() {
        ConcurrentMap<String,GMPUser> contents = sessionCache.asMap();
        contents.forEach((s, gmpUser) -> {
            sessionDAO.updateSession(gmpUser.getSession());
        });
    }

}
