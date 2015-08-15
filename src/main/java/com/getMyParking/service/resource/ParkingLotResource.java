package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.*;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/parking_lot")
@Api(value = "/v1/parking_lot", description = "Parking Lot Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingLotResource {

    private ParkingDAO parkingDAO;
    private ParkingLotDAO parkingLotDAO;

    @Inject
    public ParkingLotResource(ParkingDAO parkingDAO, ParkingLotDAO parkingLotDAO) {
        this.parkingDAO = parkingDAO;
        this.parkingLotDAO = parkingLotDAO;
    }

    @GET
    @Path("/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Parking Lot Id", response = ParkingLotEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ParkingLotEntity getParkingLotById(@PathParam("parkingLotId")int id,
                                              @Auth GMPUser gmpUser) {
        ParkingLotEntity parkingLotEntity = parkingLotDAO.findById(id);
        if (parkingLotEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        for (ParkingSubLotEntity parkingSubLot : parkingLotEntity.getParkingSubLots()) {
            if (gmpUser.getParkingSubLotIds().contains(parkingSubLot.getId())) {
                return parkingLotEntity;
            }
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    @Path("/parking/{parkingId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or update the parking lot", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingLot(@ApiParam("Parking Lot") @Valid ParkingLotEntity parkingLot, @PathParam("parkingId") int parkingId) {
        ParkingEntity parking = parkingDAO.findById(parkingId);
        if (parking == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingLot.setParking(parking);
            parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
        }
        return parkingLot.getId();
    }

    @DELETE
    @Path("/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "delete the parking lot")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public void deleteParkingLot(@PathParam("parkingLotId")int parkingLotId) {
        parkingLotDAO.deleteById(parkingLotId);
    }



    /*@Path("/{parkingLotId}/report")
    @GET
    @Timed
    @UnitOfWork
    public ParkingReport report( @PathParam("parkingLotId") Integer parkingLotId,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        return parkingEventDAO.createReport(parkingLot,fromDate.get(),toDate.get());
    }*/

}
