package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingPassMasterDAO;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
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

    @Inject
    public ParkingPassResource(ParkingPassDAO parkingPassDAO, ParkingPassMasterDAO parkingPassMasterDAO) {
        this.parkingPassDAO = parkingPassDAO;
        this.parkingPassMasterDAO = parkingPassMasterDAO;
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
    public ParkingPassEntity getParkingPass(@PathParam("parkingPassId")int id) {
        ParkingPassEntity parkingPassEntity = parkingPassDAO.findById(id);
        if (parkingPassEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return parkingPassEntity;
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
    public List<ParkingPassEntity> getActiveParkingPass(@QueryParam("parkingPassIds")String id) {
        List<String> parkingPassIds = Splitter.on(",").splitToList(id);
        List<ParkingPassEntity> parkingPassList = parkingPassDAO.findByIds(parkingPassIds);
        if (parkingPassList == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
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

}
