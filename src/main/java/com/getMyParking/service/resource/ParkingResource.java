package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.entity.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/parking")
@Api(value = "/v1/parking", description = "Parking Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingResource {

    private ParkingDAO parkingDAO;
    private CompanyDAO companyDAO;
    private ParkingEventDAO parkingEventDAO;

    @Inject
    public ParkingResource(ParkingDAO parkingDAO, CompanyDAO companyDAO, ParkingEventDAO parkingEventDAO) {
        this.parkingDAO = parkingDAO;
        this.companyDAO = companyDAO;
        this.parkingEventDAO = parkingEventDAO;
    }

    @ApiOperation(value = "Get Parking by Parking Id", response = ParkingEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GET
    @Path("/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public ParkingEntity getParkingById(@ApiParam(value = "Parking id")@PathParam("parkingId")int id) {
        ParkingEntity parkingEntity = parkingDAO.findById(id);
        if (parkingEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return parkingEntity;
    }

    @ApiOperation(value = "Save or Upadate the Parking Object", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Path("/company/{companyId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParking(@ApiParam(value = "Valid Parking Object")@Valid ParkingEntity parking,
                                   @ApiParam(value = "Company Id parking belongs to ")@PathParam("companyId") int companyId) {
        CompanyEntity company = companyDAO.findById(companyId);
        if (company == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parking.setCompany(company);
            parkingDAO.saveOrUpdateParking(parking);
        }
        return parking.getId();
    }

    @ApiOperation(value = "Delete Parking by Parking Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DELETE
    @Path("/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParking(@PathParam("parkingId")int parkingId) {
        parkingDAO.deleteById(parkingId);
    }

    @Path("/{parkingId}/report")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by parking", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ParkingReport report( @PathParam("parkingId") Integer parkingId,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        return parkingEventDAO.createParkingReport(parkingId,fromDate.get(),toDate.get());
    }

    @Path("/{parkingId}/report/details")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by parking clubed by types", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<ParkingReportGroup> report( @PathParam("parkingId") Integer parkingId, @QueryParam("types")String types,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        List<String> typesList = Splitter.on(',').splitToList(types);
        return parkingEventDAO.createParkingReportByTypes(parkingId,fromDate.get(),toDate.get(),typesList);
    }

    @Path("/{parkingId}/parking_pass")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update parking entity with parking pass", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingPass(@ApiParam("Parking Sub Lot")@Valid ParkingPassMasterEntity parkingPassMasterEntity,
                                       @PathParam("parkingId") int parkingId) {
        ParkingEntity parking = parkingDAO.findById(parkingId);
        if (parking == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingPassMasterEntity.setParking(parking);
            parking.getParkingPasses().add(parkingPassMasterEntity);
            parkingDAO.saveOrUpdateParking(parking);
        }
        return parkingPassMasterEntity.getId();
    }
}
