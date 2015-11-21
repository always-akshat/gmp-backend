package com.getMyParking.quartz;

import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Injector;
import org.hibernate.Hibernate;
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
import java.util.Map;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class AutoCheckoutJob implements Job {

    private ParkingEventDAO parkingEventDAO;
    private ParkingSubLotDAO parkingSubLotDAO;
    private SessionFactory sessionFactory;

    public AutoCheckoutJob() {
        Injector injector = GuiceHelper.getInjector();
        sessionFactory = injector.getInstance(SessionFactory.class);
        parkingEventDAO = injector.getInstance(ParkingEventDAO.class);
        parkingSubLotDAO = injector.getInstance(ParkingSubLotDAO.class);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int parkingSubLotId = jobDataMap.getInt("parkingSubLotId");
        DateTime toDate = DateTime.now();
        DateTime fromDate = toDate.minusDays(1);
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingSubLotId, fromDate, toDate);
        ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
        int count = 0;

        Map<Integer,List<PricingSlotEntity>> pricingSlotMap = Maps.newHashMap();

        Hibernate.initialize(parkingSubLot.getPricingSlots());

        for (PricingSlotEntity pricingSlot : parkingSubLot.getPricingSlots()) {
            if (pricingSlotMap.containsKey(pricingSlot.getDay())) {
                pricingSlotMap.get(pricingSlot.getDay()).add(pricingSlot);
            } else {
                pricingSlotMap.put(pricingSlot.getDay(),Lists.newArrayList(pricingSlot));
            }
        }

        if (parkingEvents != null && parkingEvents.size() > 0) {

            for (ParkingEventEntity oldParkingEvent : parkingEvents) {
                ParkingEventEntity parkingEvent = new ParkingEventEntity();
                parkingEvent.setEventTime(DateTime.now());
                parkingEvent.setEventType("CHECKED_OUT");
                parkingEvent.setParkingSubLotId(parkingSubLot.getId());
                parkingEvent.setRegistrationNumber(oldParkingEvent.getRegistrationNumber());
                parkingEvent.setSerialNumber(oldParkingEvent.getSerialNumber());
                parkingEvent.setType(oldParkingEvent.getType());
                parkingEvent.setOperatorName(oldParkingEvent.getOperatorName());
                parkingEvent.setSpecial("AC");
                parkingEvent.setSubLotType(oldParkingEvent.getSubLotType());
                parkingEvent.setCompanyId(oldParkingEvent.getCompanyId());
                parkingEvent.setParkingId(oldParkingEvent.getParkingId());
                parkingEvent.setParkingLotId(oldParkingEvent.getParkingLotId());
                if (oldParkingEvent.getShiftNumber() != null) {
                    parkingEvent.setShiftNumber(oldParkingEvent.getShiftNumber());
                }
                if (oldParkingEvent.getParkingPassId() != null) {
                    parkingEvent.setParkingPassId(oldParkingEvent.getParkingPassId());
                }
                parkingEvent.setCost(parkingSubLot.getAutoCheckoutCost());
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
