package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingPassMasterDAO;
import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 22/11/15.
 */
@Path("/v1/parking_pass_master")
@Api(value = "/v1/parking_pass", description = "Parking Pass Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingPassMasterResource {


    private ParkingDAO parkingDAO;
    private ParkingPassMasterDAO parkingPassMasterDAO;

    @Inject
    public ParkingPassMasterResource(ParkingDAO parkingDAO, ParkingPassMasterDAO parkingPassMasterDAO) {
        this.parkingDAO = parkingDAO;
        this.parkingPassMasterDAO = parkingPassMasterDAO;
    }

    @POST
    @Path("/{parkingId}")
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

}
