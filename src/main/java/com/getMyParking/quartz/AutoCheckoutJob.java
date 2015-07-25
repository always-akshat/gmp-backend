package com.getMyParking.quartz;

import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.common.base.Function;
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

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class AutoCheckoutJob implements Job {

    private ParkingEventDAO parkingEventDAO;
    private ParkingLotDAO parkingLotDAO;
    private SessionFactory sessionFactory;

    public AutoCheckoutJob() {
        Injector injector = GuiceHelper.getInjector();
        sessionFactory = injector.getInstance(SessionFactory.class);
        parkingEventDAO = injector.getInstance(ParkingEventDAO.class);
        parkingLotDAO = injector.getInstance(ParkingLotDAO.class);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int parkingLotId = jobDataMap.getInt("parkingLotId");
        DateTime toDate = DateTime.now();
        DateTime fromDate = toDate.minusDays(1);
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingLotId, fromDate, toDate);
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        int count = 0;
        if (parkingEvents != null && parkingEvents.size() > 0) {

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
                List<PricingSlotEntity> currentPricingSlot = Lists.newArrayList();
                for (PricingSlotEntity pricingSlot : pricingSlotList) {
                    if (pricingSlot.getVehicleType().equalsIgnoreCase(oldParkingEvent.getVehicleType())) {
                        currentPricingSlot.add(pricingSlot);
                    }
                }
                parkingEvent.setCost(new BigDecimal(PricingFunction.calculateInitialCost(currentPricingSlot, DateTime.now())));
                parkingEvent.setUpdatedTime(DateTime.now());
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
