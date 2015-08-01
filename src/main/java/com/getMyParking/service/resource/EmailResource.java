package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.email.SESService;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 01/08/15.
 */
@Path("/v1/email")
public class EmailResource {

    private SESService sesService;

    @Inject
    public EmailResource(SESService sesService) {
        this.sesService = sesService;
    }

    @Path("/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @POST
    @UnitOfWork
    public void sendEmail(@PathParam("parkingLotId")IntParam parkingLotId) {
        try {
            sesService.sendEmail(parkingLotId.get());
        } catch (Exception ex) {
            throw new WebApplicationException("Email Sending Failed", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }



}
