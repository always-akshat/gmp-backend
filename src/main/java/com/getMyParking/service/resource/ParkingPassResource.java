package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingPassMasterDAO;
import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    private ParkingDAO parkingDAO;

    @Inject
    public ParkingPassResource(ParkingPassDAO parkingPassDAO, ParkingPassMasterDAO parkingPassMasterDAO,ParkingDAO parkingDAO) {
        this.parkingPassDAO = parkingPassDAO;
        this.parkingPassMasterDAO = parkingPassMasterDAO;
        this.parkingDAO = parkingDAO;
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
                                       @PathParam("parkingPassMasterId") Integer parkingPassMasterId) {
        ParkingPassMasterEntity parkingPassMaster = parkingPassMasterDAO.findById(parkingPassMasterId);
        if (parkingPassMaster == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingPassEntity.setParkingPassMaster(parkingPassMaster);
            parkingPassDAO.saveOrUpdateParkingPass(parkingPassEntity);
        }
        return parkingPassEntity.getId();
    }

    @POST
    @Path("/master/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update a parking pass master entity", response = Integer.class)
    public int saveOrUpdateParkingPassMaster(@ApiParam("Parking Pass Master Entity") @Valid ParkingPassMasterEntity parkingPassMasterEntity,
                                       @PathParam("parkingId") Integer parkingId) {
        ParkingEntity parkingEntity = parkingDAO.findById(parkingId);
        if (parkingEntity == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingPassMasterEntity.setParking(parkingEntity);
            parkingPassMasterDAO.saveOrUpdateParking(parkingPassMasterEntity);
        }
        return parkingPassMasterEntity.getId();
    }


    @Path("/updateCounter/{parkingPassId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update a parking pass counter", response = Void.class)
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
