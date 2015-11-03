package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.GetParkingEventResponse;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingSubLotEntity;
import com.getMyParking.processor.ParkingEventProcessor;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Collections;
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
    private ParkingEventProcessor parkingEventProcessor;

    @Inject
    public ParkingEventResource(ParkingEventDAO parkingEventDAO, ParkingSubLotDAO parkingSubLotDAO, ParkingPassDAO parkingPassDAO,
                                ParkingEventProcessor parkingEventProcessor) {
        this.parkingEventDAO = parkingEventDAO;
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.parkingPassDAO = parkingPassDAO;
        this.parkingEventProcessor = parkingEventProcessor;
    }

    @GET
    @Path("/parking_sub_lot/active")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Events by last update time stamp that have only checkin events", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
    })
    public List<GetParkingEventResponse> getActiveParkingEventById(@QueryParam("duration")IntParam duration,
                                                             @Auth GMPUser gmpUser) {
        List<Integer> parkingSubLotIds = gmpUser.getParkingSubLotIds();
        List<GetParkingEventResponse> parkingEventsResponseList = Lists.newArrayList();
        for (Integer parkingSubLotId : parkingSubLotIds) {
            GetParkingEventResponse parkingEventResponse = new GetParkingEventResponse();
            parkingEventResponse.setParkingSubLotId(parkingSubLotId);
            List<ParkingEventEntity> parkingEvents =
                    parkingEventDAO.getParkingEvents(parkingSubLotId,DateTime.now().minusDays(duration.get()),DateTime.now());
            parkingEvents = Lists.newArrayList(Sets.newHashSet(parkingEvents));
            for (ParkingEventEntity parkingEvent : parkingEvents) {
                parkingEvent.setParkingSubLotId(parkingSubLotId);
                if (parkingEvent.getParkingPass() != null)
                    parkingEvent.setParkingPassId(parkingEvent.getParkingPass().getId());
            }
            Collections.sort(parkingEvents);
            parkingEventResponse.setParkingEvents(parkingEvents);
            parkingEventsResponseList.add(parkingEventResponse);
        }

        return parkingEventsResponseList;
    }

    @GET
    @Path("/parking_sub_lot/")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Events by last update time stamp", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
    })
    public List<GetParkingEventResponse> getParkingEventById(@QueryParam("lastUpdateTime")DateTimeParam lastUpdateTime,
                                                        @Auth GMPUser gmpUser) {
        List<Integer> parkingSubLotIds = gmpUser.getParkingSubLotIds();
        List<GetParkingEventResponse> parkingEventsResponseList = Lists.newArrayList();
        for (Integer parkingSubLotId : parkingSubLotIds) {
            GetParkingEventResponse parkingEventResponse = new GetParkingEventResponse();
            parkingEventResponse.setParkingSubLotId(parkingSubLotId);
            List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingSubLotId, lastUpdateTime.get());
            parkingEvents = Lists.newArrayList(Sets.newHashSet(parkingEvents));
            for (ParkingEventEntity parkingEvent : parkingEvents) {
                parkingEvent.setParkingSubLotId(parkingSubLotId);
                if (parkingEvent.getParkingPass() != null)
                    parkingEvent.setParkingPassId(parkingEvent.getParkingPass().getId());
            }
            Collections.sort(parkingEvents);
            parkingEventResponse.setParkingEvents(parkingEvents);
            parkingEventsResponseList.add(parkingEventResponse);
        }
        return parkingEventsResponseList;
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
    public BigInteger saveOrUpdateParkingEvent(@ApiParam (value = "Parking Event") @Valid ParkingEventEntity parkingEvent,
                                               @PathParam("parkingSubLotId")int parkingSubLotId,
                                               @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingSubLotId)) {
            ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
            if (parkingSubLot == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                return saveParkingEvent(parkingEvent,parkingSubLot);
            }
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("/parking_sub_lot/{parkingSubLotId}/batch")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update Parking Events",
            notes = "Creates or Update a list of parking events, returns a list of parking event ",response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<BigInteger> saveOrUpdateParkingEvents(@ApiParam (value = "Parking Event") @Valid List<ParkingEventEntity> parkingEvents,
                                               @PathParam("parkingSubLotId")int parkingSubLotId,
                                               @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingSubLotId)) {
            ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
            List<BigInteger> parkingEventIds = Lists.newArrayList();
            if (parkingSubLot == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                for (ParkingEventEntity parkingEvent : parkingEvents) {
                    try {
                        parkingEventIds.add(saveParkingEvent(parkingEvent, parkingSubLot));
                    } catch (WebApplicationException ex) {
                        parkingEventIds.add(BigInteger.ZERO);
                    }
                }
            }
            return parkingEventIds;
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    private BigInteger saveParkingEvent(ParkingEventEntity parkingEvent, ParkingSubLotEntity parkingSubLot) {
        if (parkingEvent.getId() == null) {
            List<ParkingEventEntity> pe = parkingEventDAO.findBySerialNumberAndEventType(parkingSubLot.getId(),
                    parkingEvent.getEventType(), parkingEvent.getSerialNumber());
            if (pe != null && pe.size() > 0) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }
        }
        parkingEvent.setParkingSubLot(parkingSubLot);
        parkingEvent.setUpdatedTime(DateTime.now());
        if (parkingEvent.getParkingPassId() != null) {
            parkingEvent.setParkingPass(parkingPassDAO.findById(parkingEvent.getParkingPassId()));
        }
        parkingEventProcessor.processEvent(parkingEvent);
        parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
        return parkingEvent.getId();
    }

    @PUT
    @Path("/{parkingEventId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update Parking Event",
            notes = "Update a parking event, returns a parking event id",response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public BigInteger updateParkingEvent(@ApiParam (value = "Parking Event") @Valid ParkingEventEntity parkingEvent,
                                               @PathParam("parkingEventId") BigInteger parkingEventId,
                                               @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().contains(parkingEvent.getParkingSubLotId())) {
            ParkingEventEntity pe = parkingEventDAO.findById(parkingEventId);
            pe.copy(parkingEvent);
            pe.setUpdatedTime(DateTime.now());
            if (parkingEvent.getParkingSubLotId() != null && pe.getParkingSubLot() != null
                && !pe.getParkingSubLot().getId().equals(parkingEvent.getParkingSubLotId())) {
                ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingEvent.getParkingSubLotId());
                pe.setParkingSubLot(parkingSubLot);
            }
            if (parkingEvent.getParkingPassId() != null && pe.getParkingPass() != null
                && !pe.getParkingPass().getId().equals(parkingEvent.getParkingPassId())) {
                ParkingPassEntity parkingPass = parkingPassDAO.findById(parkingEvent.getParkingSubLotId());
                pe.setParkingPass(parkingPass);
            }
            parkingEventDAO.saveOrUpdateParkingEvent(pe);
            return pe.getId();
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
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
    public BigInteger saveOrUpdateParkingPassEvent(@ApiParam (value = "Parking Event") @Valid ParkingEventEntity parkingEvent,
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
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

}
