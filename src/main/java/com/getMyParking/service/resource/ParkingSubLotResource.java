package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.dao.ParkingSubLotUserAccessDAO;
import com.getMyParking.entity.*;
import com.getMyParking.entity.reports.ParkingReport;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;
import org.hibernate.Hibernate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 15/08/15.
 */
@Path("/v1/parking_sub_lot")
@Api(value = "/v1/parking_sub_lot", description = "Parking Sub Lot Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingSubLotResource {

    private ParkingSubLotDAO parkingSubLotDAO;
    private ParkingLotDAO parkingLotDAO;
    private ParkingEventDAO parkingEventDAO;
    private ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO;

    @Inject
    public ParkingSubLotResource(ParkingSubLotDAO parkingSubLotDAO, ParkingLotDAO parkingLotDAO,
                                 ParkingEventDAO parkingEventDAO, ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO) {
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.parkingLotDAO = parkingLotDAO;
        this.parkingEventDAO = parkingEventDAO;
        this.parkingSubLotUserAccessDAO = parkingSubLotUserAccessDAO;
    }

    @GET
    @Path("/{parkingSubLotIds}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Get parking sub lot entity by Id", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public List<ParkingSubLotEntity> getParkingSubLotsById(@PathParam("parkingSubLotIds")String ids,
                                                           @Auth GMPUser gmpUser) {
        List<String> parkingLotSubIds = Splitter.on(",").splitToList(ids);
        List<ParkingSubLotEntity> parkingSubLotEntities = Lists.newArrayList();
        for (String id : parkingLotSubIds) {
            Integer parkingLotSubId = Integer.parseInt(id);
            if (gmpUser.getParkingSubLotIds().contains(parkingLotSubId)) {
                ParkingSubLotEntity parkingSubLotEntity = parkingSubLotDAO.findById(parkingLotSubId);
                if (parkingSubLotEntity == null) {
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
                } else {
                    parkingSubLotEntities.add(parkingSubLotEntity);
                }
            } else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
        return parkingSubLotEntities;
    }

    @Path("/parking_lot/{parkingLotId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Save or Update parking sub lot entity, returns Id on successful completion", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateParkingSubLot(@ApiParam ("Parking Sub Lot")@Valid ParkingSubLotEntity parkingSubLot,
                                         @PathParam("parkingLotId") int parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        if (parkingLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingSubLot.setParkingLot(parkingLot);
            parkingSubLotDAO.saveOrUpdateParkingLot(parkingSubLot);
        }
        return parkingLot.getId();
    }

    @Path("/{parkingSubLotId}/pricing_slot")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update parking sub lot entity with pricing slot", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdatePricingSlot(@ApiParam("Parking Sub Lot")@Valid PricingSlotEntity pricingSlotEntity, @PathParam("parkingSubLotId") int parkingSubLotId) {
        ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
        if (parkingSubLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            Hibernate.initialize(parkingSubLot.getPricingSlots());
            pricingSlotEntity.setParkingSubLot(parkingSubLot);
            for (PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGrids()) {
                priceGridEntity.setPricingSlot(pricingSlotEntity);
            }
            parkingSubLot.getPricingSlots().add(pricingSlotEntity);
            parkingSubLotDAO.saveOrUpdateParkingLot(parkingSubLot);
        }
        return pricingSlotEntity.getId();
    }

    @Path("/{parkingSubLotId}/receipt_content")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @ApiOperation(value = "Update parking sub lot entity with receipt content", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public int saveOrUpdateReceiptContent(@ApiParam("Parking Sub Lot")@Valid ReceiptContentEntity receiptContentEntity, @PathParam("parkingSubLotId") int parkingSubLotId) {
        ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
        if (parkingSubLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            Hibernate.initialize(parkingSubLot.getReceiptContents());
            receiptContentEntity.setParkingSubLot(parkingSubLot);
            parkingSubLot.getReceiptContents().add(receiptContentEntity);
            parkingSubLotDAO.saveOrUpdateParkingLot(parkingSubLot);
        }
        return receiptContentEntity.getId();
    }

    @Path("/{parkingSubLotId}/report")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by parking sub lot", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ParkingReport report( @PathParam("parkingSubLotId") Integer parkingSubLotId,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        ParkingSubLotEntity parkingSubLot = parkingSubLotDAO.findById(parkingSubLotId);
        BigDecimal nFactor = new BigDecimal(Float.toString(parkingSubLot.getParkingLot().getParking().getnFactor()));
        ParkingReport parkingReport = parkingEventDAO.createParkingSubLotReport(parkingSubLotId,fromDate.get(),toDate.get());
        parkingReport.setCheckOutRevenue(parkingReport.getCheckOutRevenue().multiply(nFactor));
        parkingReport.setCheckInRevenue(parkingReport.getCheckInRevenue().multiply(nFactor));
        return parkingReport;
    }
}
