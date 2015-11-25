package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.SessionDAO;
import com.getMyParking.dto.ActiveSessions;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rahulgupta.s on 25/11/15.
 */
@Api(value = "/v1/session", description = "Session Resource")
@Path("/v1/session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    private SessionDAO sessionDAO;

    @Inject
    public SessionResource(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @GET
    @UnitOfWork
    @Timed
    @ExceptionMetered
    @Path("/active")
    @ApiOperation(value = "Get active Session Details", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    public List<ActiveSessions> getActiveSessions() {
        return sessionDAO.getActiveSessions();
    }
}
