package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entities.Parking;
import com.getMyParking.entities.ParkingLot;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by rahulgupta.s on 15/03/15.
 */

@Path("/v1/parking_lot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParkingResource {

    private ParkingDAO parkingDAO;
    private ParkingLotDAO parkingLotDAO;

    public ParkingResource(ParkingDAO parkingDAO, ParkingLotDAO parkingLotDAO) {
        this.parkingDAO = parkingDAO;
        this.parkingLotDAO = parkingLotDAO;
    }

    @Path("/{parkingLotId}/parking")
    @GET
    @Timed
    @UnitOfWork
    public List<Parking> getParkings(@PathParam("parkingLotId") Integer parkingLotId) throws JsonProcessingException {
        return parkingDAO.getParkingByParkingLotId(parkingLotId);
    }

    @Path("/{parkingLotId}")
    @GET
    @Timed
    @UnitOfWork
    public ParkingLot getParkingLot(@PathParam("parkingLotId") Integer parkingLotId) {
        return parkingLotDAO.getParkingLot(parkingLotId);
    }

    @POST
    @Timed
    @UnitOfWork
    public void createParkingLot(@Valid ParkingLot parkingLot){
        parkingLotDAO.saveParkingLot(parkingLot);
    }

    @Path("/{parkingLotId}/parking")
    @POST
    @Timed
    @UnitOfWork
    public void createParking(@Valid List<Parking> parking, @PathParam("parkingLotId") Integer parkingLotId) {
        parkingDAO.saveParking(parking, parkingLotId);
    }


}
