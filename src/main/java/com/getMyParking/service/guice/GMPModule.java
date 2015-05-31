package com.getMyParking.service.guice;

import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

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

    }

    @Provides
    public SessionFactory getSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
