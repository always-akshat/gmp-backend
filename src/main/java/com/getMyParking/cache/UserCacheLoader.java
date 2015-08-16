package com.getMyParking.cache;

import com.getMyParking.dao.SessionDAO;
import com.getMyParking.entity.SessionEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.cache.CacheLoader;
import com.google.inject.Inject;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * Created by rahulgupta.s on 04/06/15.
 */
public class UserCacheLoader extends CacheLoader<String,GMPUser> {

    private SessionDAO sessionDAO;

    @Inject
    public UserCacheLoader(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public GMPUser load(@Nullable String authToken) throws Exception {
        SessionEntity sessionEntity = sessionDAO.findByAuthToken(authToken);
        if (sessionEntity != null && DateTime.now().isBefore(new DateTime(sessionEntity.getValidTime()))) {
            UserB2BEntity user = sessionEntity.getUserB2BEntity();
            return new GMPUser(user,authToken);
        }
        throw new Exception("INVALID_AUTH_TOKEN");
    }
}
