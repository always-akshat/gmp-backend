package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.entity.reports.ParkingReportByUser;
import com.getMyParking.dao.ParkingSubLotUserAccessDAO;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.reports.ParkingReport;
import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
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
@Api(value = "/v1/company", description = "Company Resource")
@Path("/v1/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {

    private CompanyDAO companyDAO;
    private ParkingEventDAO parkingEventDAO;
    private ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO;

    @Inject
    public CompanyResource(CompanyDAO companyDAO, ParkingEventDAO parkingEventDAO, ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO) {
        this.companyDAO = companyDAO;
        this.parkingEventDAO = parkingEventDAO;
        this.parkingSubLotUserAccessDAO = parkingSubLotUserAccessDAO;
    }

    @ApiOperation(value = "Get Company by Company Id", response = CompanyEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GET
    @Path("/{companyId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public CompanyEntity getCompanyById(@ApiParam("Company Id")@PathParam("companyId")int id) {
        CompanyEntity companyEntity = companyDAO.findById(id);
        if (companyEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return companyEntity;
    }

    @ApiOperation(value = "Create or Update Company Object Api", notes = "saves or updates the company object, returns the id of the company created", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateCompany(@ApiParam("Company Object to be created")@Valid CompanyEntity company) {
        companyDAO.saveOrUpdateCompany(company);
        return company.getId();
    }

    @ApiOperation(value = "Delete the Company Object by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DELETE
    @Path("/{companyId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteCompany(@ApiParam("Company Id to be deleted")@PathParam("companyId")int companyId) {
        companyDAO.deleteById(companyId);
    }

    @Path("/{companyId}/report")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by company", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ParkingReport report( @PathParam("companyId") Integer companyId,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        return parkingEventDAO.createCompanyReport(companyId,fromDate.get(),toDate.get());
    }

    @Path("/{companyId}/userReport/details")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by company for all operators ", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<ParkingReportByUser> userReport( @PathParam("companyId") Integer companyId,
                                                      @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        List<ParkingSubLotUserAccessEntity> userAccessList = parkingSubLotUserAccessDAO.getAllUsersWithAccessToCompany(companyId);
        return parkingEventDAO.createParkingReportByUsers(fromDate.get(), toDate.get(), userAccessList);
    }


}
