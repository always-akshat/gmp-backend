package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.entity.AdminReport;
import com.getMyParking.entity.ParkingReport;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by karan on 23/10/15.
 */
@Api(value = "/v1/admin", description = "Company Resource")
@Path("/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private ParkingEventDAO parkingEventDAO;

    @Inject
    public AdminResource(ParkingEventDAO parkingEventDAO) {
        this.parkingEventDAO = parkingEventDAO;
    }

    @Path("/report/{offset}")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "get admin report", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<AdminReport> parkingReport(@PathParam("offset") int pageNo,
                                             @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate){
        return parkingEventDAO.createParkingReportForAdmin(pageNo,fromDate.get(),toDate.get());
    }


}
