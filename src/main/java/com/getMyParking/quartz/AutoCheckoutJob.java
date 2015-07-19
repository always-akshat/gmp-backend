package com.getMyParking.quartz;

import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class AutoCheckoutJob implements Job {

    private ParkingEventDAO parkingEventDAO;
    private SessionFactory sessionFactory;

    public AutoCheckoutJob() {
        Injector injector = GuiceHelper.getInjector();
        sessionFactory = injector.getInstance(SessionFactory.class);
        parkingEventDAO = injector.getInstance(ParkingEventDAO.class);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int parkingLotId = jobDataMap.getInt("parkingLotId");
        DateTime fromDate = DateTime.now();
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());
        DateTime toDate = fromDate.plusDays(1);
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingLotId, fromDate, toDate);
        int count = 0;
        if (parkingEvents != null && parkingEvents.size() > 0) {
            ParkingLotEntity parkingLot = parkingEvents.get(0).getParkingLotByParkingLotId();

            for (ParkingEventEntity oldParkingEvent : parkingEvents) {
                ParkingEventEntity parkingEvent = new ParkingEventEntity();
                parkingEvent.setEventTime(DateTime.now());
                parkingEvent.setEventType("CHECKED_OUT");
                parkingEvent.setParkingLotByParkingLotId(parkingLot);
                parkingEvent.setRegistrationNumber(oldParkingEvent.getRegistrationNumber());
                parkingEvent.setSerialNumber(oldParkingEvent.getSerialNumber());
                parkingEvent.setType(oldParkingEvent.getType());
                parkingEvent.setVehicleType(oldParkingEvent.getVehicleType());
                if (oldParkingEvent.getShiftNumber() != null) {
                    parkingEvent.setShiftNumber(oldParkingEvent.getShiftNumber());
                }
                if (oldParkingEvent.getParkingPassByParkingPassId() != null) {
                    parkingEvent.setParkingPassByParkingPassId(oldParkingEvent.getParkingPassByParkingPassId());
                }
                List<PricingSlotEntity> pricingSlotList = Lists.newArrayList(parkingLot.getPricingSlotsById());
                parkingEvent.setCost(new BigDecimal(PricingFunction.calculateInitialCost(pricingSlotList, DateTime.now())));
                parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
                count ++;
                if (count == 20) {
                    session.flush();
                    session.clear();
                }
            }
        }
        session.flush();
        session.clear();
        session.close();

    }

}
