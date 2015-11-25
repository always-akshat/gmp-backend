package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.*;
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
import org.joda.time.DateTimeZone;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;

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
                                                        @Auth GMPUser gmpUser, @HeaderParam("DEVICE_ID") String deviceId) {
        List<Integer> parkingSubLotIds = gmpUser.getParkingSubLotIds();
        List<GetParkingEventResponse> parkingEventsResponseList = Lists.newArrayList();
        for (Integer parkingSubLotId : parkingSubLotIds) {
            GetParkingEventResponse parkingEventResponse = new GetParkingEventResponse();
            parkingEventResponse.setParkingSubLotId(parkingSubLotId);
            List<ParkingEventEntity> parkingEvents = parkingEventDAO.getParkingEvents(parkingSubLotId, lastUpdateTime.get());
            parkingEvents = Lists.newArrayList(Sets.newHashSet(parkingEvents));
            for (ParkingEventEntity parkingEvent : parkingEvents) {
                parkingEvent.setParkingSubLotId(parkingSubLotId);
                if (parkingEvent.getParkingPass() != null) {
                    parkingEvent.setParkingPassId(parkingEvent.getParkingPass().getId());
                    parkingEvent.getParkingPass().setParkingPassMasterId(
                            parkingEvent.getParkingPass().getParkingPassMaster().getId()
                    );
                }
            }
            Collections.sort(parkingEvents);
            parkingEventResponse.setParkingEvents(parkingEvents);
            parkingEventsResponseList.add(parkingEventResponse);
        }
        SessionEntity session = gmpUser.getSession();
        session.setLastAccessTime(DateTime.now());
        session.setDeviceId(deviceId);
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
    @Path("/batch")
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
                                               @Auth GMPUser gmpUser, @HeaderParam("DEVICE_ID") String deviceId) {
        List<Integer> parkingSubLotIds = parkingEvents.stream().map(ParkingEventEntity::getParkingSubLotId)
                .distinct().collect(Collectors.toList());
        if (gmpUser.getParkingSubLotIds().containsAll(parkingSubLotIds)) {
            Map<Integer,ParkingSubLotEntity> parkingSubLotMap = parkingSubLotDAO.findByIds(parkingSubLotIds).stream().collect(
                    Collectors.toMap(ParkingSubLotEntity::getId,Function.<ParkingSubLotEntity>identity()));
            List<BigInteger> parkingEventIds = Lists.newArrayList();
            if (parkingSubLotMap.size() != parkingSubLotIds.size()) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else {
                for (ParkingEventEntity parkingEvent : parkingEvents) {
                    try {
                        parkingEventIds.add(saveParkingEvent(parkingEvent, parkingSubLotMap.get(parkingEvent.getParkingSubLotId())));
                    } catch (WebApplicationException ex) {
                        parkingEventIds.add(BigInteger.valueOf(-409));
                    }
                }
            }

            SessionEntity session = gmpUser.getSession();
            if ( session.getLastTransactionTime() != null &&
                    session.getLastTransactionTime().toDateTime(DateTimeZone.forOffsetHoursMinutes(5,30)).dayOfMonth() !=
                    DateTime.now(DateTimeZone.forOffsetHoursMinutes(5,30)).dayOfMonth()) {
                session.setTransactionCount(1);
            } else {
                session.setTransactionCount(session.getTransactionCount() + 1);
            }

            session.setLastAccessedParkingLotId(parkingEvents.get(0).getParkingLotId());
            session.setLastTransactionTime(DateTime.now());
            session.setDeviceId(deviceId);
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
        parkingEvent.setParkingSubLotId(parkingSubLot.getId());
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
            if (parkingEvent.getParkingSubLotId() != null && pe.getParkingSubLotId() != null
                && !pe.getParkingSubLotId().equals(parkingEvent.getParkingSubLotId())) {
                ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingEvent.getParkingSubLotId());
                pe.setParkingSubLotId(parkingSubLot.getId());
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
                parkingEvent.setParkingSubLotId(parkingLotId);
                parkingEvent.setParkingPass(parkingPass);
                parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
            }
            return parkingEvent.getId();
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @GET
    @Path("/")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Events by last update time stamp", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UnAuthorized"),
    })
    public List<ParkingEventEntity> getParkingEventsById(@QueryParam("parkingSubLotId")Optional<IntParam> parkingSubLotId,
                                                         @QueryParam("parkingLotId")Optional<IntParam> parkingLotId,
                                                         @QueryParam("parkingId")Optional<IntParam> parkingId,
                                                         @QueryParam("companyId")Optional<IntParam> companyId,
                                                         @QueryParam("registrationNumber") Optional<String> registrationNumber,
                                                         @QueryParam("fromDate") Optional<DateTimeParam> fromDate,
                                                         @QueryParam("toDate") Optional<DateTimeParam> toDate,
                                                         @QueryParam("eventType") Optional<String> eventType,
                                                         @QueryParam("pageNumber") @DefaultValue("0") IntParam pageNumberParam,
                                                         @QueryParam("pageSize") @DefaultValue("30") IntParam pageSizeParam,
                                                         @Auth GMPUser gmpUser) {

        if (companyId.isPresent() && !gmpUser.getCompanyIds().contains(companyId.get().get())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (parkingId.isPresent() && !gmpUser.getParkingIds().contains(parkingId.get().get())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (parkingLotId.isPresent() && !gmpUser.getParkingLotIds().contains(parkingLotId.get().get())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (parkingSubLotId.isPresent() && !gmpUser.getParkingSubLotIds().contains(parkingSubLotId.get().get())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (registrationNumber.isPresent() && !parkingLotId.isPresent()) {
            throw new WebApplicationException("Invalid Combination of parameters", Response.Status.BAD_REQUEST);
        }

        Integer pageSize = pageSizeParam.get() > 30 ? 30 : pageSizeParam.get();

        return parkingEventDAO.searchParkingEvents(companyId, parkingId, parkingLotId, parkingSubLotId, registrationNumber,
                fromDate, toDate, eventType, pageNumberParam.get(), pageSize);
    }

}
