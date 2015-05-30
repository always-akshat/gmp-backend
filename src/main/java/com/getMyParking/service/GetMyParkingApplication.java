package com.getMyParking.service;

import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.entity.*;
import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.getMyParking.service.guice.GMPModule;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
                Company.class, Parking.class, ParkingEvent.class, ParkingLot.class,
                ParkingLotHasUserB2B.class, ParkingLotHasUserB2BPK.class, ParkingPass.class,
                ParkingPassPK.class, ParkingPassMaster.class, PriceGrid.class, PricingSlot.class,
                ReceiptContent.class, Session.class, UserB2B.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(GetMyParkingConfiguration getMyParkingConfiguration) {
                return getMyParkingConfiguration.getDataSourceFactory();
            }
        };

        guiceBundle = GuiceBundle.<GetMyParkingConfiguration>newBuilder()
                                .addModule(new GMPModule())
                                .enableAutoConfig(getClass().getPackage().getName())
                                .setConfigClass(GetMyParkingConfiguration.class)
                                .build();

        guiceBundle.getInjector().injectMembers(hibernateBundle.getSessionFactory());

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(flywayBundle);
    }

    @Override
    public void run(GetMyParkingConfiguration getMyParkingConfiguration, Environment environment) throws Exception {


    }
}
