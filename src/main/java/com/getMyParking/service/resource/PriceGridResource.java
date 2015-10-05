package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.PriceGridDAO;
import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/price_grid")
@Api(value = "/v1/price_grid", description = "Parking Price Grid Resource")
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
    @ApiOperation(value = "Get Price Grid by price grid id", response = PriceGridEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
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
    @ApiOperation(value = "Save or Update Price Grid Entity", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public void saveOrUpdatePriceGrid(@ApiParam ("PriceGrid Entity")@Valid PriceGridEntity priceGrid) {
        priceGridDAO.saveOrUpdatePricingSlot(priceGrid);
    }

    @DELETE
    @Path("/{priceGridId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Delete Price Grid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public void deletePriceGrid(@PathParam("priceGridId")int priceGridId) {
        priceGridDAO.deleteById(priceGridId);
    }
}
