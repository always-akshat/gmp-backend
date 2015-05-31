package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.entity.CompanyEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

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
    @UnitOfWork
    public CompanyEntity getCompanyById(@PathParam("companyId")int id) {
        CompanyEntity companyEntity = companyDAO.findById(id);
        if (companyEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return companyEntity;
    }

    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateCompany(@Valid CompanyEntity company) {
        companyDAO.saveOrUpdateCompany(company);
        return company.getId();
    }

    @DELETE
    @Path("/{companyId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteCompany(@PathParam("companyId")int companyId) {
        companyDAO.deleteById(companyId);
    }


}
