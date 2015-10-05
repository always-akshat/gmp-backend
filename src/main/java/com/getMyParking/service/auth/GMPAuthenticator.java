package com.getMyParking.service.auth;

import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.netflix.governator.guice.lazy.LazySingleton;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
@LazySingleton
public class GMPAuthenticator implements Authenticator<GMPCredentials,GMPUser> {

    private LoadingCache<String,GMPUser> authTokenCache;
    private static final Logger logger = LoggerFactory.getLogger(GMPAuthenticator.class);

    @Inject
    public GMPAuthenticator(@Named("authTokenCache")LoadingCache<String, GMPUser> authTokenCache) {
        this.authTokenCache = authTokenCache;
    }

    @Override
    public Optional<GMPUser> authenticate(GMPCredentials gmpCredentials) throws AuthenticationException {
        try {
            GMPUser gmpUser = authTokenCache.get(gmpCredentials.getAuthToken());
            logger.info("Current Date Time {} Valid Date Time {}", DateTime.now(), gmpUser.getValidTime());
            if (DateTime.now().isBefore(gmpUser.getValidTime()))
                return Optional.of(gmpUser);
            else
                return Optional.absent();
        } catch (ExecutionException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
