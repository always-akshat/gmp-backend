package com.getMyParking.service.guice;

import com.getMyParking.cache.UserCacheLoader;
import com.getMyParking.service.auth.GMPAuthenticator;
import com.getMyParking.service.auth.GMPUser;
import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.netflix.governator.guice.lazy.LazySingleton;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
public class GMPModule extends AbstractModule {

    private HibernateBundle<GetMyParkingConfiguration> hibernateBundle;

    public GMPModule(HibernateBundle<GetMyParkingConfiguration> hibernateBundle) {
        this.hibernateBundle = hibernateBundle;
    }

    @Override
    protected void configure() {
        bind(Authenticator.class).to(GMPAuthenticator.class);
    }

    @LazySingleton
    @Provides
    @Named("authTokenCache")
    public LoadingCache<String,GMPUser> getAuthTokenCache(UserCacheLoader userCacheLoader) {
        return CacheBuilder.<String,GMPUser>newBuilder()
                .maximumSize(5000)
                .refreshAfterWrite(1, TimeUnit.DAYS)
                .build(userCacheLoader);
    }

    @Provides
    public SessionFactory getSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
