package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.entity.Company;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.jersey.params.IntParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {

    private CompanyDAO companyDAO;

    @Inject
    public CompanyResource(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    @GET
    @Path("/{companyId}")
    @Timed
    @ExceptionMetered
    public Company getCompanyById(@PathParam("companyId")Optional<IntParam> companyId) {
        if (companyId.isPresent()) {
            return companyDAO.findById(companyId.get().get());
        } else {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Company Id Empty").build());
        }
    }

    @POST
    @Timed
    @ExceptionMetered
    public void saveOrUpdateCompany(@Valid Company company) {
        companyDAO.saveOrUpdateCompany(company);
    }

    @DELETE
    @Path("/{companyId}")
    @Timed
    @ExceptionMetered
    public void deleteCompany(@PathParam("companyId")Optional<IntParam> companyId) {
        if (companyId.isPresent()) {
            companyDAO.deleteById(companyId.get().get());
        } else {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Company Id Empty").build());
        }
    }


}
