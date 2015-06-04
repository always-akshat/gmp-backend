package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/parking_event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingEventResource {

    private ParkingEventDAO parkingEventDAO;
    private ParkingLotDAO parkingLotDAO;
    private ParkingPassDAO parkingPassDAO;

    @Inject
    public ParkingEventResource(ParkingEventDAO parkingEventDAO, ParkingLotDAO parkingLotDAO, ParkingPassDAO parkingPassDAO) {
        this.parkingEventDAO = parkingEventDAO;
        this.parkingLotDAO = parkingLotDAO;
        this.parkingPassDAO = parkingPassDAO;
    }

    @GET
    @Path("/parking_lot/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public List<ParkingEventEntity> getParkingEventById(@PathParam("parkingLotId")int parkingLotId,
                                                        @QueryParam("lastUpdateTime")String lastUpdateTimeStr,
                                                        @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingLotIds().contains(parkingLotId)) {
            DateTime lastUpdateTime = DateTime.parse(lastUpdateTimeStr);
            return parkingEventDAO.getParkingEvents(parkingLotId, lastUpdateTime);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/parking_lot/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParkingEvent(@Valid ParkingEventEntity parkingEvent,
                                        @PathParam("parkingLotId")int parkingLotId,
                                        @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingLotIds().contains(parkingLotId)) {
            ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
            if (parkingLot == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                parkingEvent.setParkingLotByParkingLotId(parkingLot);
                parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
            }
            return parkingEvent.getId();
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/parking_lot/{parkingLotId}/parking_pass/{parkingPassId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParkingPassEvent(@Valid ParkingEventEntity parkingEvent,
                                            @PathParam("parkingLotId")int parkingLotId,
                                            @PathParam("parkingPassId")int parkingPassId,
                                            @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingLotIds().contains(parkingLotId)) {
            ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
            ParkingPassEntity parkingPass = parkingPassDAO.findById(parkingPassId);
            if (parkingLot == null && parkingPass == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                parkingEvent.setParkingLotByParkingLotId(parkingLot);
                parkingEvent.setParkingPassByParkingPassId(parkingPass);
                parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
            }
            return parkingEvent.getId();
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}
