package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.PricingSlotDAO;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/pricing_slot")
@Api(value = "/v1/pricing_slot", description = "Parking Pricing Slot Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PriceSlotResource {

    private PricingSlotDAO pricingSlotDAO;

    @Inject
    public PriceSlotResource(PricingSlotDAO pricingSlotDAO) {
        this.pricingSlotDAO = pricingSlotDAO;
    }

    @GET
    @Path("/{pricingSlotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get Price Grid by price grid id", response = PriceGridEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
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
    @ApiOperation(value = "Associate price grid with price slot", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdatePriceGrid(@ApiParam ("Price Grid Entity") @Valid PriceGridEntity priceGridEntity, @PathParam("pricingSlotId")int pricingSlotId) {
        PricingSlotEntity priceSlot = pricingSlotDAO.findById(pricingSlotId);
        if (priceSlot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            priceGridEntity.setPricingSlot(priceSlot);
            priceSlot.getPriceGrids().add(priceGridEntity);
            pricingSlotDAO.saveOrUpdatePricingSlot(priceSlot);
        }
        return priceGridEntity.getId();
    }

    @PUT
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update Pricing Slot, returns a pricing slot id", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public Integer saveOrUpdatePricingSlot(@ApiParam ("Price Slot Entity") @Valid PricingSlotEntity pricingSlot) {
        pricingSlotDAO.saveOrUpdatePricingSlot(pricingSlot);
        return pricingSlot.getId();
    }

    @DELETE
    @Path("/{pricingSlotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Delete Pricing Slot")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public void deletePricingLot(@PathParam("pricingSlotId")int pricingSlotId) {
        pricingSlotDAO.deleteById(pricingSlotId);
    }

    @GET
    @Path("/cost")
    @UnitOfWork
    public double calculateCost() {
        List<PricingSlotEntity> pricingSlotEntityList = pricingSlotDAO.findBySubLotId(22);
        Map<Integer,List<PricingSlotEntity>> slotMap =
                pricingSlotEntityList.stream().collect(Collectors.groupingBy(PricingSlotEntity::getDay));
        return PricingFunction.calculateTotalCost(slotMap, DateTime.parse("01/12/2015 02:04", DateTimeFormat.forPattern("dd/MM/YYYY HH:mm")), DateTime.now());
    }


}
