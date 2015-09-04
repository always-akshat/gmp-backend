package com.getMyParking.service;

import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.*;
import com.getMyParking.quartz.AutoCheckoutJob;
import com.getMyParking.service.auth.GMPAuthFactory;
import com.getMyParking.service.auth.GMPAuthenticator;
import com.getMyParking.service.configuration.GetMyParkingConfiguration;
import com.getMyParking.service.exceptionMapper.AuthenticationExceptionMapper;
import com.getMyParking.service.guice.GMPModule;
import com.getMyParking.service.guice.GuiceHelper;
import com.getMyParking.service.managed.ManagedQuartzScheduler;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.dropwizard.guice.InjectorFactory;
import com.netflix.governator.guice.LifecycleInjector;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

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

    private GuiceBundle<GetMyParkingConfiguration> guiceBundle;

    @Override
    public void initialize(Bootstrap<GetMyParkingConfiguration> bootstrap) {

        FlywayBundle<GetMyParkingConfiguration> flywayBundle = new FlywayBundle<GetMyParkingConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(GetMyParkingConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(GetMyParkingConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        };

        HibernateBundle<GetMyParkingConfiguration> hibernateBundle = new HibernateBundle<GetMyParkingConfiguration>(
                CompanyEntity.class, ParkingEntity.class, ParkingEventEntity.class, ParkingLotEntity.class,
                ParkingSubLotUserAccessEntity.class, ParkingPassEntity.class, ParkingPassMasterEntity.class,
                PriceGridEntity.class, PricingSlotEntity.class, ReceiptContentEntity.class,
                SessionEntity.class, UserB2BEntity.class, ParkingSubLotEntity.class, StyleMasterEntity.class,
                UserAccessEntity.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(GetMyParkingConfiguration getMyParkingConfiguration) {
                return getMyParkingConfiguration.getDataSourceFactory();
            }
        };

        guiceBundle = GuiceBundle.<GetMyParkingConfiguration>newBuilder()
                                 .addModule(new GMPModule(hibernateBundle,bootstrap))
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

        bootstrap.addBundle(new SwaggerBundle<GetMyParkingConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(GetMyParkingConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(flywayBundle);
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(GetMyParkingConfiguration getMyParkingConfiguration, Environment environment) throws Exception {

        environment.jersey().register(AuthFactory.binder(
                new GMPAuthFactory(guiceBundle.getInjector().getInstance(GMPAuthenticator.class), "Oh! You Missed Something..")));
        ManagedQuartzScheduler quartzScheduler = guiceBundle.getInjector().getInstance(ManagedQuartzScheduler.class);
        Scheduler scheduler = quartzScheduler.getScheduler();
        GuiceHelper.setInjector(guiceBundle.getInjector());
        Session session = guiceBundle.getInjector().getInstance(SessionFactory.class).openSession();
        ManagedSessionContext.bind(session);
        ParkingSubLotDAO parkingSubLotDAO = guiceBundle.getInjector().getInstance(ParkingSubLotDAO.class);
        List<ParkingSubLotEntity> parkingSubLots =
                Lists.newArrayList(Sets.newHashSet(parkingSubLotDAO.getAllParkingLots()));

        for (ParkingSubLotEntity parkingSubLot : parkingSubLots) {

            if (parkingSubLot.getAutoCheckoutTime() == null) continue;

            JobDetail jobDetail = newJob(AutoCheckoutJob.class)
                    .usingJobData("parkingSubLotId", parkingSubLot.getId())
                    .withIdentity("autoCheckout", String.valueOf(parkingSubLot.getId()))
                    .build();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parkingSubLot.getAutoCheckoutTime());
            String cronExpression = calendar.get(Calendar.SECOND) + " " + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.HOUR_OF_DAY) + " * * ?";
            Trigger trigger = newTrigger()
                    .withIdentity("autoCheckoutTrigger", String.valueOf(parkingSubLot.getId()))
                    .forJob(jobDetail)
                    .withSchedule(cronSchedule(cronExpression).inTimeZone(TimeZone.getTimeZone("IST")))
                    .build();
            scheduler.scheduleJob(jobDetail,trigger);
        }
        session.close();
    }
}
