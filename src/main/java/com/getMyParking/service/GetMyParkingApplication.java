package com.getMyParking.service;

import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.entity.*;
import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.getMyParking.service.guice.GMPModule;
import com.getMyParking.service.interceptors.RequestLogger;
import com.getMyParking.service.interceptors.ResponseFilter;
import com.getMyParking.service.resource.CompanyResource;
import com.getMyParking.service.resource.ParkingResource;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.dropwizard.guice.InjectorFactory;
import com.netflix.governator.guice.LifecycleInjector;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rahulgupta.s
 * Date: 14/03/15
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetMyParkingApplication extends Application<GetMyParkingConfiguration> {

    public static void main(String[] args) throws Exception {
        new GetMyParkingApplication().run(args);
    }

    private HibernateBundle<GetMyParkingConfiguration> hibernateBundle;
    private FlywayBundle<GetMyParkingConfiguration> flywayBundle;
    private GuiceBundle<GetMyParkingConfiguration> guiceBundle;

    @Override
    public void initialize(Bootstrap<GetMyParkingConfiguration> bootstrap) {

        flywayBundle = new FlywayBundle<GetMyParkingConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(GetMyParkingConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(GetMyParkingConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        };

        hibernateBundle = new HibernateBundle<GetMyParkingConfiguration>(
                CompanyEntity.class, ParkingEntity.class, ParkingEventEntity.class, ParkingLotEntity.class,
                ParkingLotHasUserB2BEntity.class, ParkingPassEntity.class, ParkingPassMasterEntity.class,
                PriceGridEntity.class, PricingSlotEntity.class, ReceiptContentEntity.class,
                SessionEntity.class, UserB2BEntity.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(GetMyParkingConfiguration getMyParkingConfiguration) {
                return getMyParkingConfiguration.getDataSourceFactory();
            }
        };

        guiceBundle = GuiceBundle.<GetMyParkingConfiguration>newBuilder()
                                 .addModule(new GMPModule(hibernateBundle))
                                 .enableAutoConfig(getClass().getPackage().getName())
                                 .setInjectorFactory(new InjectorFactory() {
                                     @Override
                                     public Injector create(Stage stage, List<Module> list) {
                                         return LifecycleInjector.builder()
                                                 .inStage(Stage.PRODUCTION)
                                                 .withModules(list)
                                                 .build()
                                                 .createInjector();
                                     }
                                 })
                                 .setConfigClass(GetMyParkingConfiguration.class)
                                 .build();

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(flywayBundle);
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(GetMyParkingConfiguration getMyParkingConfiguration, Environment environment) throws Exception {

        environment.jersey().getResourceConfig().getContainerRequestFilters().add(RequestLogger.class.getName());
        environment.jersey().getResourceConfig().getContainerResponseFilters().add(ResponseFilter.class.getName());
    }
}
