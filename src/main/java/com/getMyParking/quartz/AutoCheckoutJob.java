package com.getMyParking.quartz;

import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
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

    @Inject
    private ParkingEventDAO parkingEventDAO;

    public AutoCheckoutJob(ParkingEventDAO parkingEventDAO) {
        this.parkingEventDAO = parkingEventDAO;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int parkingLotId = jobDataMap.getInt("parkingLotId");
        DateTime fromDate = DateTime.now();
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());
        DateTime toDate = fromDate.plusDays(1);

        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingLotId, fromDate, toDate);
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
            }
        }

    }

}
