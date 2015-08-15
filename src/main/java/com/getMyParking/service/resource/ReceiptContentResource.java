package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ReceiptContentDAO;
import com.getMyParking.entity.ReceiptContentEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/receipt_content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiptContentResource {

    /*private ReceiptContentDAO receiptContentDAO;

    @Inject
    public ReceiptContentResource(ReceiptContentDAO receiptContentDAO) {
        this.receiptContentDAO = receiptContentDAO;
    }

    @GET
    @Path("/{receiptContentId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
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
    public void saveOrUpdateReceiptContent(@Valid ReceiptContentEntity receiptContent) {
        receiptContentDAO.saveOrUpdatePricingSlot(receiptContent);
    }

    @DELETE
    @Path("/{receiptContentId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deletePriceGrid(@PathParam("receiptContentId")int id) {
        receiptContentDAO.deleteById(id);
    }*/
}
