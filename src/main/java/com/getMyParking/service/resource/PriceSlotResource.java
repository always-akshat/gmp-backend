package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.PricingSlotDAO;
import com.getMyParking.entity.ParkingLotEntity;
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
@Path("/v1/pricing_slot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PriceSlotResource {

    /*private PricingSlotDAO pricingSlotDAO;

    @Inject
    public PriceSlotResource(PricingSlotDAO pricingSlotDAO) {
        this.pricingSlotDAO = pricingSlotDAO;
    }

    @GET
    @Path("/{pricingSlotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public PricingSlotEntity getPricingSlotById(@PathParam("pricingSlotId")int id) {
        PricingSlotEntity pricingSlotEntity = pricingSlotDAO.findById(id);
        if (pricingSlotEntity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return pricingSlotEntity;
    }

    @Path("/{pricingSlotId}/price_grid")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdatePriceGrid(@Valid PriceGridEntity priceGridEntity, @PathParam("parkingLotId") int parkingLotId, @PathParam("pricingSlotId")int pricingSlotId) {
        PricingSlotEntity priceSlot = pricingSlotDAO.findById(pricingSlotId);
        if (priceSlot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            priceGridEntity.setPricingSlotByPricingId(priceSlot);
            priceSlot.getPriceGridsById().add(priceGridEntity);
            pricingSlotDAO.saveOrUpdatePricingSlot(priceSlot);
        }
        return priceGridEntity.getId();
    }

    @PUT
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void saveOrUpdatePricingSlot(@Valid PricingSlotEntity pricingSlot) {
        pricingSlotDAO.saveOrUpdatePricingSlot(pricingSlot);
    }

    @DELETE
    @Path("/{pricingSlotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deletePricingLot(@PathParam("pricingSlotId")int pricingSlotId) {
        pricingSlotDAO.deleteById(pricingSlotId);
    }*/


}
