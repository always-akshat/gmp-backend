package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.*;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Path("/v1/parking_lot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingLotResource {

    private ParkingDAO parkingDAO;
    private ParkingLotDAO parkingLotDAO;
    private ParkingEventDAO parkingEventDAO;


    @Inject
    public ParkingLotResource(ParkingDAO parkingDAO, ParkingLotDAO parkingLotDAO, ParkingEventDAO parkingEventDAO) {
        this.parkingDAO = parkingDAO;
        this.parkingLotDAO = parkingLotDAO;
        this.parkingEventDAO = parkingEventDAO;
    }

    @GET
    @Path("/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public ParkingLotEntity getParkingLotById(@PathParam("parkingLotId")int id,
                                              @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingLotIds().contains(id)) {
            ParkingLotEntity parkingLotEntity = parkingLotDAO.findById(id);
            if (parkingLotEntity == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return parkingLotEntity;
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("/batch/{parkingLotIds}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public List<ParkingLotEntity> getParkingLotById(@PathParam("parkingLotIds")String ids,
                                              @Auth GMPUser gmpUser) {
        List<ParkingLotEntity> parkingLotEntityList = Lists.newArrayList();
        List<String> parkingLotIds = Splitter.on(",").splitToList(ids);
        for (String idStr : parkingLotIds) {
            int id = Integer.parseInt(idStr);
            if (gmpUser.getParkingLotIds().contains(id)) {
                ParkingLotEntity parkingLotEntity = parkingLotDAO.findById(id);
                if (parkingLotEntity == null) {
                    parkingLotEntityList.add(parkingLotEntity);
                }
            }
        }
        return parkingLotEntityList;
    }

    @Path("/parking/{parkingId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int createParkingLot(@Valid ParkingLotEntity parkingLot, @PathParam("parkingId") int parkingId) {
        ParkingEntity parking = parkingDAO.findById(parkingId);
        if (parking == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingLot.setParkingByParkingId(parking);
            parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
        }
        return parkingLot.getId();
    }

    @PUT
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void updateParkingLot(@Valid ParkingLotEntity parkingLot) {
        parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
    }

    @DELETE
    @Path("/{parkingLotId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParkingLot(@PathParam("parkingLotId")int parkingLotId) {
        parkingLotDAO.deleteById(parkingLotId);
    }

    @Path("/{parkingLotId}/parking_pass")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParkingPass(@Valid ParkingPassMasterEntity parkingPassMasterEntity, @PathParam("parkingLotId") int parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        if (parkingLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            parkingPassMasterEntity.setParkingLotByParkingLotId(parkingLot);
            parkingLot.getParkingPassMastersById().add(parkingPassMasterEntity);
            parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
        }
        return parkingPassMasterEntity.getId();
    }

    @Path("/{parkingLotId}/pricing_slot")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdatePricingSlot(@Valid PricingSlotEntity pricingSlotEntity, @PathParam("parkingLotId") int parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        if (parkingLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            pricingSlotEntity.setParkingLotByParkingLotId(parkingLot);
            for (PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGridsById()) {
                priceGridEntity.setPricingSlotByPricingId(pricingSlotEntity);
            }
            parkingLot.getPricingSlotsById().add(pricingSlotEntity);
            parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
        }
        return pricingSlotEntity.getId();
    }

    @Path("/{parkingLotId}/receipt_content")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateReceiptContent(@Valid ReceiptContentEntity receiptContentEntity, @PathParam("parkingLotId") int parkingLotId) {
        ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
        if (parkingLot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            receiptContentEntity.setParkingLotByParkingLotId(parkingLot);
            parkingLot.getReceiptContentsById().add(receiptContentEntity);
            parkingLotDAO.saveOrUpdateParkingLot(parkingLot);
        }
        return receiptContentEntity.getId();
    }

    @Path("/{parkingLotId}/report")
    @GET
    @Timed
    @UnitOfWork
    public ParkingReport report( @PathParam("parkingLotId") Integer parkingLotId,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {

        return parkingEventDAO.createReport(parkingLotId,fromDate.get(),toDate.get());
    }

}
