package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by rahulgupta.s on 17/10/15.
 */
@Path("/v1/job")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobsResource {

    private ParkingEventDAO parkingEventDAO;
    private ParkingSubLotDAO parkingSubLotDAO;

    @Inject
    public JobsResource(ParkingEventDAO parkingEventDAO, ParkingSubLotDAO parkingSubLotDAO) {
        this.parkingEventDAO = parkingEventDAO;
        this.parkingSubLotDAO = parkingSubLotDAO;
    }

    @POST
    @Path("/autocheckout")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void autoCheckoutJob(@QueryParam("parkingSubLotId") Integer parkingSubLotId) {
        DateTime toDate = DateTime.now();
        DateTime fromDate = toDate.minusDays(1);
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());

        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingSubLotId, fromDate, toDate);
        ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);

        Map<Integer,List<PricingSlotEntity>> pricingSlotMap = Maps.newHashMap();
        for (PricingSlotEntity pricingSlot : parkingSubLot.getPricingSlots()) {
            if (pricingSlotMap.containsKey(pricingSlot.getDay())) {
                pricingSlotMap.get(pricingSlot.getDay()).add(pricingSlot);
            } else {
                pricingSlotMap.put(pricingSlot.getDay(), Lists.newArrayList(pricingSlot));
            }
        }

        if (parkingEvents != null && parkingEvents.size() > 0) {

            for (ParkingEventEntity oldParkingEvent : parkingEvents) {
                ParkingEventEntity parkingEvent = new ParkingEventEntity();
                parkingEvent.setEventTime(DateTime.now());
                parkingEvent.setEventType("CHECKED_OUT");
                parkingEvent.setParkingSubLot(parkingSubLot);
                parkingEvent.setRegistrationNumber(oldParkingEvent.getRegistrationNumber());
                parkingEvent.setSerialNumber(oldParkingEvent.getSerialNumber());
                parkingEvent.setType(oldParkingEvent.getType());
                parkingEvent.setOperatorName("AUTO_CHECKOUT");
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
            }
        }
    }

    @POST
    @Path("/email")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void emailReport(@QueryParam("parkingSubLotId") Integer parkingId, @QueryParam("emailIds") String emailIds) {
        List<String> emailIdList = Splitter.on(",").splitToList(emailIds);
        DateTime toDate = DateTime.now().withTimeAtStartOfDay();
        DateTime fromDate = toDate.minusDays(1);
        fromDate = fromDate.minusSeconds(fromDate.getSecondOfDay());

        List<ParkingEventEntity> parkingEvents = parkingEventDAO.getAllParkingEventsByParking(parkingId, fromDate, toDate);

    }

}
