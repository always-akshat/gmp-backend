package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.*;
import com.getMyParking.entity.*;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by karan on 20/10/15.
 */
@Path("/v1/duplicate")
@Api(value = "/v1/duplicate", description = "Duplication Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DuplicateResource {

    private ParkingDAO parkingDAO;
    private ParkingLotDAO parkingLotDAO;
    private ParkingSubLotDAO parkingSubLotDAO;
    private CompanyDAO companyDAO;
    private PriceGridDAO priceGridDAO;
    private PricingSlotDAO pricingSlotDAO;
    private ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO;
    private ParkingPassMasterDAO parkingPassMasterDAO;
    private ParkingPassDAO parkingPassDAO;
    private ParkingEventDAO parkingEventDAO;
    private ReceiptContentDAO receiptContentDAO;

    @Inject
    public DuplicateResource(ParkingDAO parkingDAO, ParkingLotDAO parkingLotDAO, ParkingSubLotDAO parkingSubLotDAO, CompanyDAO companyDAO, PriceGridDAO priceGridDAO, PricingSlotDAO pricingSlotDAO, ParkingSubLotUserAccessDAO parkingSubLotUserAccessDAO, ParkingPassMasterDAO parkingPassMasterDAO, ParkingPassDAO parkingPassDAO, ParkingEventDAO parkingEventDAO) {
        this.parkingDAO = parkingDAO;
        this.parkingLotDAO = parkingLotDAO;
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.companyDAO = companyDAO;
        this.priceGridDAO = priceGridDAO;
        this.pricingSlotDAO = pricingSlotDAO;
        this.parkingSubLotUserAccessDAO = parkingSubLotUserAccessDAO;
        this.parkingPassMasterDAO = parkingPassMasterDAO;
        this.parkingPassDAO = parkingPassDAO;
        this.parkingEventDAO = parkingEventDAO;
    }

    @ApiOperation(value = "Save or Upadate the Parking Object", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Path("/company/{companyId}")
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public int saveOrUpdateParking(@ApiParam(value = "Valid Parking Object")@Valid ParkingEntity parking,
                                   @ApiParam(value = "Company Id parking belongs to ")@PathParam("companyId") int companyId) {
        CompanyEntity company = companyDAO.findById(companyId);
        if (company == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else {
            Set<ParkingLotEntity> parkingLotEntity = parking.getParkingLots();
            parking.setParkingLots(null);
            parking.setCompany(company);
            parkingDAO.saveOrUpdateParking(parking);
            for(ParkingLotEntity temp : parkingLotEntity){
                Set<ParkingSubLotEntity> parkingSubLotEntities = temp.getParkingSubLots();
                temp.setId(null);
                temp.setParking(parking);
                temp.setParkingSubLots(null);
                parkingLotDAO.saveOrUpdateParkingLot(temp);
                for(ParkingSubLotEntity temp1 : parkingSubLotEntities){
                    temp1.setParkingLot(temp);
                    temp1.setId(null);
                    for(PricingSlotEntity pricingSlotEntity : temp1.getPricingSlots()){
                        pricingSlotEntity.setId(null);
                        for(PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGrids()){
                            priceGridEntity.setId(null);
                        }
                    }
                    parkingSubLotDAO.saveOrUpdateParkingLot(temp1);
                    System.out.println(temp1.getId());
                }
            }
        }
        return parking.getId();
    }

    @ApiOperation(value = "Delete Parking by Parking Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DELETE
    @Path("/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParking(@PathParam("parkingId")int parkingId) {
        ParkingEntity parking = parkingDAO.findById(parkingId);
        Set<ParkingLotEntity> parkingLotEntity = parking.getParkingLots();
        parkingSubLotUserAccessDAO.deleteByParkingId(parkingId);

        for(ParkingLotEntity temp : parkingLotEntity){
            Set<ParkingSubLotEntity> parkingSubLotEntities = temp.getParkingSubLots();
            for(ParkingSubLotEntity temp1 : parkingSubLotEntities){
                for(PricingSlotEntity pricingSlotEntity : temp1.getPricingSlots()){
                    for(PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGrids()){
                        priceGridDAO.deleteById(priceGridEntity.getId());
                    }
                    pricingSlotDAO.deleteById(pricingSlotEntity.getId());
                }
                for(ReceiptContentEntity receiptContentEntity : temp1.getReceiptContents()){
                    receiptContentDAO.deleteById(receiptContentEntity.getId());
                }
                parkingEventDAO.deleteBySubLotId(temp1.getId());
                parkingSubLotDAO.deleteParkingSublotById(temp1.getId());
            }
            parkingLotDAO.deleteById(temp.getId());
        }
        List<ParkingPassMasterEntity>  parkingPassMasterEntities = parkingPassMasterDAO.findByParkingId(parkingId);
        for(ParkingPassMasterEntity parkingPassMasterEntity : parkingPassMasterEntities){
            parkingPassDAO.deleteByMasterId(parkingPassMasterEntity.getId());
        }
        parkingPassMasterDAO.deleteParkingPassMasterByParkingId(parkingId);
        parkingDAO.deleteById(parkingId);
    }


    @ApiOperation(value = "Delete Parking by Parking Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DELETE
    @Path("parking_lot/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParkingLot(@PathParam("parkingId")int parkingLotId) {
        ParkingLotEntity temp = parkingLotDAO.findById(parkingLotId);

            Set<ParkingSubLotEntity> parkingSubLotEntities = temp.getParkingSubLots();
            for(ParkingSubLotEntity temp1 : parkingSubLotEntities){
                for(PricingSlotEntity pricingSlotEntity : temp1.getPricingSlots()){
                    for(PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGrids()){
                        priceGridDAO.deleteById(priceGridEntity.getId());
                    }
                    pricingSlotDAO.deleteById(pricingSlotEntity.getId());
                }
                for(ReceiptContentEntity receiptContentEntity : temp1.getReceiptContents()){
                    receiptContentDAO.deleteById(receiptContentEntity.getId());
                }
                parkingEventDAO.deleteBySubLotId(temp1.getId());
                parkingSubLotDAO.deleteParkingSublotById(temp1.getId());
            }
            parkingLotDAO.deleteById(temp.getId());

    }

    @ApiOperation(value = "Delete Parking by Parking Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DELETE
    @Path("parking_sub_lot/{parkingId}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void deleteParkingSubLot(@PathParam("parkingId")int parkingSubLotId) {

        ParkingSubLotEntity temp1 = parkingSubLotDAO.findById(parkingSubLotId);

            for(PricingSlotEntity pricingSlotEntity : temp1.getPricingSlots()){
                for(PriceGridEntity priceGridEntity : pricingSlotEntity.getPriceGrids()){
                    priceGridDAO.deleteById(priceGridEntity.getId());
                }
                pricingSlotDAO.deleteById(pricingSlotEntity.getId());
            }
            for(ReceiptContentEntity receiptContentEntity : temp1.getReceiptContents()){
                receiptContentDAO.deleteById(receiptContentEntity.getId());
            }
            parkingEventDAO.deleteBySubLotId(temp1.getId());
            parkingSubLotDAO.deleteParkingSublotById(temp1.getId());

    }

}
