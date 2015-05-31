package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.PriceGridDAO;
import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/price_grid")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PriceGridResource {

    private PriceGridDAO priceGridDAO;

    @Inject
    public PriceGridResource(PriceGridDAO priceGridDAO) {
        this.priceGridDAO = priceGridDAO;
    }

    @GET
    @Path("/{priceGridId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public PriceGridEntity getPriceGrid(@PathParam("priceGridId")int id) {
        PriceGridEntity priceGrid = priceGridDAO.findById(id);
        if (priceGrid == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return priceGrid;
    }

    @PUT
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void saveOrUpdatePriceGrid(@Valid PriceGridEntity priceGrid) {
        priceGridDAO.saveOrUpdatePricingSlot(priceGrid);
    }

    @DELETE
    @Path("/{priceGridId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deletePriceGrid(@PathParam("priceGridId")int priceGridId) {
        priceGridDAO.deleteById(priceGridId);
    }
}
