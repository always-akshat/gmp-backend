package com.getMyParking.service.managed;

import io.dropwizard.lifecycle.Managed;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class ManagedQuartzScheduler implements Managed {

    private Scheduler scheduler;

    @Override
    public void start() throws Exception {
        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
