package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingPassMasterDAO;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.getMyParking.processor.ParkingEventProcessor;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Created by rahulgupta.s on 01/06/15.
 */
@Path("/v1/parking_pass")
@Api(value = "/v1/parking_pass", description = "Parking Pass Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingPassResource {

    private ParkingPassDAO parkingPassDAO;
    private ParkingPassMasterDAO parkingPassMasterDAO;
    private ParkingEventProcessor parkingEventProcessor;

    @Inject
    public ParkingPassResource(ParkingPassDAO parkingPassDAO, ParkingPassMasterDAO parkingPassMasterDAO,
                               ParkingEventProcessor parkingEventProcessor) {
        this.parkingPassDAO = parkingPassDAO;
        this.parkingPassMasterDAO = parkingPassMasterDAO;
        this.parkingEventProcessor = parkingEventProcessor;
    }

    @GET
    @Path("/{parkingPassId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Pass Id", response = ParkingPassEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<ParkingPassEntity> getParkingPass(@PathParam("parkingPassId")String ids) {
        List<String> parkingPassIds = Splitter.on(",").splitToList(ids);
        List<ParkingPassEntity> parkingPassList = parkingPassDAO.findByPassIds(parkingPassIds);
        if (parkingPassList == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            for (ParkingPassEntity passEntity : parkingPassList) {
                passEntity.setParkingPassMasterId(passEntity.getParkingPassMaster().getId());
            }
        }
        return parkingPassList;
    }

    @GET
    @Path("/active")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Returns the list of active passes", response = ParkingPassEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<ParkingPassEntity> getActiveParkingPass(@QueryParam("parkingPassIds")String id, @Auth GMPUser  gmpUser) {
        List<String> parkingPassIds = Splitter.on(",").splitToList(id);
        List<ParkingPassEntity> parkingPassList = parkingPassDAO.findByIds(parkingPassIds);
        if (parkingPassList == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            parkingPassList = Lists.newArrayList(Sets.newHashSet(parkingPassList));
            for (ParkingPassEntity passEntity : parkingPassList) {
                passEntity.setParkingPassMasterId(passEntity.getParkingPassMaster().getId());
            }
        }
        return parkingPassList;
    }

    @GET
    @Path("/")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Search Parking Pass", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Forbidden"),
    })
    public List<ParkingPassEntity> getParkingEventsById(@QueryParam("parkingId")Optional<IntParam> parkingId,
                                                         @QueryParam("registrationNumber") Optional<String> registrationNumber,
                                                        @QueryParam("isDeleted") Optional<IntParam> isDeleted,
                                                         @QueryParam("pageNumber") @DefaultValue("0") IntParam pageNumberParam,
                                                         @QueryParam("pageSize") @DefaultValue("30") IntParam pageSizeParam,
                                                         @Auth GMPUser gmpUser) {

        if (parkingId.isPresent() && !gmpUser.getParkingIds().contains(parkingId.get().get())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        Integer pageSize = pageSizeParam.get() > 30 ? 30 : pageSizeParam.get();

        return parkingPassDAO.searchParkingPass(parkingId, registrationNumber, isDeleted, pageNumberParam.get(), pageSize);
    }

    @POST
    @Path("/parking_pass_master/{parkingPassMasterId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update a parking pass master entity", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingPass(@ApiParam("Parking Pass Entity") @Valid ParkingPassEntity parkingPassEntity,
                                       @PathParam("parkingPassMasterId") Integer parkingPassMasterId, @Auth GMPUser gmpUser) {
        ParkingPassMasterEntity parkingPassMaster = parkingPassMasterDAO.findById(parkingPassMasterId);
        if (parkingPassMaster == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingPassEntity.setParkingPassMaster(parkingPassMaster);
            if (parkingPassEntity.getIsDeleted() == null) parkingPassEntity.setIsDeleted(0);

            String eventType;
            if (parkingPassEntity.getId() == null || parkingPassEntity.getId().equals(0)) {
                eventType = "PASS_CREATE";
            } else {
                eventType = "PASS_UPDATE";
            }
            parkingPassDAO.saveOrUpdateParkingPass(parkingPassEntity);
            parkingEventProcessor.createParkingPassEvents(parkingPassEntity, gmpUser, eventType);

        }
        return parkingPassEntity.getId();
    }

    @POST
    @Path("/updateCounter/{parkingPassId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update a parking pass counter", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public void saveParkingPassCounter(@FormParam("value") int counter,
                                       @PathParam("parkingPassId") Integer parkingPassId) {
        ParkingPassEntity parkingPass = parkingPassDAO.findById(parkingPassId);
        if (parkingPass.getCounter() < counter) {
            parkingPass.setCounter(counter);
            parkingPassDAO.saveOrUpdateParkingPass(parkingPass);
        }
    }

}
