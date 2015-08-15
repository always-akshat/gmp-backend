package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Api(value = "/v1/parking_event", description = "Parking Event Resource")
@Path("/v1/parking_event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingEventResource {

    private ParkingEventDAO parkingEventDAO;
    private ParkingSubLotDAO parkingSubLotDAO;
    private ParkingPassDAO parkingPassDAO;

    @Inject
    public ParkingEventResource(ParkingEventDAO parkingEventDAO, ParkingSubLotDAO parkingSubLotDAO, ParkingPassDAO parkingPassDAO) {
        this.parkingEventDAO = parkingEventDAO;
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.parkingPassDAO = parkingPassDAO;
    }

    @GET
    @Path("/parking_sub_lot/{parkingSubLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Events by last update time stamp", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
    })
    public List<ParkingEventEntity> getParkingEventById(@PathParam("parkingSubLotId")int parkingSubLotId,
                                                        @QueryParam("lastUpdateTime")DateTimeParam lastUpdateTime,
                                                        @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingSubLotId)) {
            List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingSubLotId, lastUpdateTime.get());
            for (ParkingEventEntity parkingEvent : parkingEvents) {
                parkingEvent.setParkingSubLotId(parkingSubLotId);
                if (parkingEvent.getParkingPass() != null)
                    parkingEvent.setParkingPassId(parkingEvent.getParkingPass().getId());
            }
            return parkingEvents;
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/parking_sub_lot/{parkingSubLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update Parking Event",
            notes = "Creates or Update a parking event, returns a parking event id",response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingEvent(@ApiParam (value = "Parking Event") @Valid ParkingEventEntity parkingEvent,
                                        @PathParam("parkingSubLotId")int parkingSubLotId,
                                        @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingSubLotId)) {
            ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
            if (parkingSubLot == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                if (parkingEvent.getId() == 0) {
                    ParkingEventEntity pe = parkingEventDAO.findBySerialNumberAndEventType(parkingSubLotId,
                            parkingEvent.getEventType(), parkingEvent.getSerialNumber());
                    if (pe != null)
                        throw new WebApplicationException(Response.Status.CONFLICT);
                }
                parkingEvent.setParkingSubLot(parkingSubLot);
                parkingEvent.setUpdatedTime(DateTime.now());
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
    @ApiOperation(value = "Save or Update Parking Event",
            notes = "Creates or Update a parking event that has parking pass associated with it, returns a parking event id",
            response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingPassEvent(@ApiParam (value = "Parking Event") @Valid ParkingEventEntity parkingEvent,
                                            @PathParam("parkingLotId")int parkingLotId,
                                            @PathParam("parkingPassId")int parkingPassId,
                                            @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingLotId)) {
            ParkingSubLotEntity parkingLot = parkingSubLotDAO.findById(parkingLotId);
            ParkingPassEntity parkingPass = parkingPassDAO.findById(parkingPassId);
            if (parkingLot == null && parkingPass == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                parkingEvent.setParkingSubLot(parkingLot);
                parkingEvent.setParkingPass(parkingPass);
                parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
            }
            return parkingEvent.getId();
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}
