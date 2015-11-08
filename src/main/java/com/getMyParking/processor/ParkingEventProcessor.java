package com.getMyParking.processor;

import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingSubLotDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 03/11/15.
 */
public class ParkingEventProcessor {

    private static final String CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+%";
    private ParkingSubLotDAO parkingSubLotDAO;
    private ParkingEventDAO parkingEventDAO;

    @Inject
    public ParkingEventProcessor(ParkingSubLotDAO parkingSubLotDAO, ParkingEventDAO parkingEventDAO) {
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.parkingEventDAO = parkingEventDAO;
    }

    public void processEvent(ParkingEventEntity parkingEvent) {

        if (parkingEvent.getEventType().equalsIgnoreCase("PASS_PAID")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            parkingEvent.getParkingPass().setIsPaid(1);
        } else if (parkingEvent.getEventType().equalsIgnoreCase("PASS_DELETED")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            parkingEvent.getParkingPass().setIsDeleted(1);
        }
    }

    public void createParkingPassEvents(ParkingPassEntity parkingPass, GMPUser user, String eventType) {

        ParkingEventEntity parkingEvent = new ParkingEventEntity();
        Integer parkingId = parkingPass.getParkingPassMaster().getParking().getId();
        parkingEvent.setCompanyId(user.getCompanyIds().get(0));
        parkingEvent.setParkingId(parkingId);
        parkingEvent.setParkingLotId(user.getParkingLotIds().get(0));
        parkingEvent.setParkingSubLot(parkingSubLotDAO.getSubLotBy(
                parkingPass.getParkingPassMaster().getVehicleType(),user.getParkingSubLotIds(), parkingId));
        parkingEvent.setParkingPass(parkingPass);

        int days = getDays();
        String day = encode(days, true);
        int hours = DateTime.now().getMinuteOfDay();
        String hour = encode(hours, true);
        if (eventType.equalsIgnoreCase("PASS_CREATE"))
            parkingEvent.setSerialNumber("" + parkingPass.getId() + day + hour + "P_C");
        else if (eventType.equalsIgnoreCase("PASS_UPDATE"))
            parkingEvent.setSerialNumber("" + parkingPass.getId() + day + hour + "P_U");
        parkingEvent.setEventType(eventType.toUpperCase());
        parkingEvent.setEventTime(parkingPass.getCreatedAt());
        parkingEvent.setOperatorName(parkingPass.getOperatorName());
        parkingEvent.setSubLotType(parkingPass.getParkingPassMaster().getVehicleType());
        parkingEvent.setType("PASS");
        parkingEvent.setRegistrationNumber(parkingPass.getRegistrationNumber());
        if (parkingPass.getMobileNumber() != null) parkingEvent.setMobileNumber(parkingPass.getMobileNumber());
        parkingEvent.setCost(BigDecimal.ZERO);
        parkingEvent.setUpdatedTime(DateTime.now());

        parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);
    }

    public int getDays() {
        DateTime start = new DateTime(2015, 8, 20, 0, 0, 0);
        DateTime end = DateTime.now();
        return Days.daysBetween(start.withTimeAtStartOfDay(),
                end.withTimeAtStartOfDay()).getDays();
    }

    public String encode(int num, boolean appendRequired) {
        final int B = CODE.length();
        boolean isAppendRequired = false;
        if (num <= 37 && appendRequired)
            isAppendRequired = true;
        StringBuilder sb = new StringBuilder();
        while (num != -1) {
            sb.append(CODE.charAt(num % B));
            num /= B;
            if (num == 0)
                num = -1;
        }
        if (isAppendRequired)
            sb.append("A");
        return sb.reverse().toString();
    }
}
