package com.getMyParking.processor;

import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.google.inject.Inject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 03/11/15.
 */
public class ParkingEventProcessor {

    private ParkingPassDAO parkingPassDAO;

    @Inject
    public ParkingEventProcessor(ParkingPassDAO parkingPassDAO) {
        this.parkingPassDAO = parkingPassDAO;
    }

    public void processEvent(ParkingEventEntity parkingEvent) {

        if (parkingEvent.getEventType().equalsIgnoreCase("PASS_PAID")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            parkingEvent.getParkingPass().setIsPaid(1);
        } else if (parkingEvent.getEventType().equalsIgnoreCase("PASS_DELETED")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            parkingEvent.getParkingPass().setIsDeleted(1);
        }

    }
}
