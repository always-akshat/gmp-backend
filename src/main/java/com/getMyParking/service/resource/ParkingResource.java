package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.ParkingEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/parking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingResource {

    private ParkingDAO parkingDAO;
    private CompanyDAO companyDAO;

    @Inject
    public ParkingResource(ParkingDAO parkingDAO, CompanyDAO companyDAO) {
        this.parkingDAO = parkingDAO;
        this.companyDAO = companyDAO;
    }

    @GET
    @Path("/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public ParkingEntity getParkingById(@PathParam("parkingId")int id) {
        ParkingEntity parkingEntity = parkingDAO.findById(id);
        if (parkingEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return parkingEntity;
    }

    @Path("/company/{companyId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParking(@Valid ParkingEntity parking, @PathParam("companyId") int companyId) {
        CompanyEntity company = companyDAO.findById(companyId);
        if (company == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parking.setCompanyByCompanyId(company);
            parkingDAO.saveOrUpdateParking(parking);
        }
        return parking.getId();
    }

    @DELETE
    @Path("/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParking(@PathParam("parkingId")int parkingId) {
        parkingDAO.deleteById(parkingId);
    }
}
