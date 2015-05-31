package com.getMyParking.service;

import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.entity.*;
import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.getMyParking.service.resource.CompanyResource;
import com.getMyParking.service.resource.ParkingResource;
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

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(flywayBundle);
    }

    @Override
    public void run(GetMyParkingConfiguration getMyParkingConfiguration, Environment environment) throws Exception {

        CompanyDAO companyDAO = new CompanyDAO(hibernateBundle.getSessionFactory());
        ParkingDAO parkingDAO = new ParkingDAO(hibernateBundle.getSessionFactory());

        ParkingResource parkingResource = new ParkingResource(parkingDAO,companyDAO);
        CompanyResource companyResource = new CompanyResource(companyDAO);

        environment.jersey().register(parkingResource);
        environment.jersey().register(companyResource);
    }
}
