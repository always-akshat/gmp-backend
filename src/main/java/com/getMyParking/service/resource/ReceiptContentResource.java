package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ReceiptContentDAO;
import com.getMyParking.entity.ReceiptContentEntity;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/receipt_content")
@Api(value = "/v1/receipt_content", description = "Receipt Content Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiptContentResource {

    private ReceiptContentDAO receiptContentDAO;

    @Inject
    public ReceiptContentResource(ReceiptContentDAO receiptContentDAO) {
        this.receiptContentDAO = receiptContentDAO;
    }

    @GET
    @Path("/{receiptContentId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get receipt content by Id",response = ReceiptContentEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ReceiptContentEntity getReceiptContent(@PathParam("receiptContentId")int id) {
        ReceiptContentEntity receiptContent = receiptContentDAO.findById(id);
        if (receiptContent == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return receiptContent;
    }

    @PUT
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or update receipt content object, returns receipt content id", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public Integer saveOrUpdateReceiptContent(@Valid ReceiptContentEntity receiptContent) {
        receiptContentDAO.saveOrUpdatePricingSlot(receiptContent);
        return receiptContent.getId();
    }

    @DELETE
    @Path("/{receiptContentId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Delete Receipt Content")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public void deletePriceGrid(@PathParam("receiptContentId")int id) {
        receiptContentDAO.deleteById(id);
    }
}
