package com.getMyParking.service.managed;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.lifecycle.Managed;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
@Singleton
public class ManagedQuartzScheduler implements Managed {

    private Scheduler scheduler;

    @Inject
    public ManagedQuartzScheduler() throws Exception{
        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
    }

    @Override
    public void start() throws Exception {
        scheduler.start();
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
