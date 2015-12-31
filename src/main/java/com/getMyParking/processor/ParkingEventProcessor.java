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
import java.util.List;

/**
 * Created by rahulgupta.s on 03/11/15.
 */
public class ParkingEventProcessor {

    private static final String CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+%";
    private ParkingSubLotDAO parkingSubLotDAO;
    private ParkingEventDAO parkingEventDAO;
    private ParkingPassDAO parkingPassDAO;

    @Inject
    public ParkingEventProcessor(ParkingSubLotDAO parkingSubLotDAO, ParkingEventDAO parkingEventDAO,
                                 ParkingPassDAO parkingPassDAO) {
        this.parkingSubLotDAO = parkingSubLotDAO;
        this.parkingEventDAO = parkingEventDAO;
        this.parkingPassDAO = parkingPassDAO;
    }

    public void processEvent(ParkingEventEntity parkingEvent) {

        if (parkingEvent.getEventType().equalsIgnoreCase("PASS_PAID")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            parkingEvent.getParkingPass().setIsPaid(1);
            parkingEvent.getParkingPass().setOfflinePaymentId(parkingEvent.getSpecial());
        } else if (parkingEvent.getEventType().equalsIgnoreCase("PASS_DELETED")) {
            if (parkingEvent.getParkingPass() == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            List<ParkingPassEntity> passEntityList = parkingPassDAO.getAllPassesWithRegistrationNumberAndParkingId(
                    parkingEvent.getParkingPass().getRegistrationNumber(), parkingEvent.getParkingId()
            );
            passEntityList.forEach( parkingPassEntity -> {
                parkingPassEntity.setIsDeleted(1);
                parkingPassDAO.saveOrUpdateParkingPass(parkingPassEntity);
            });
        }
    }

    public void createParkingPassEvents(ParkingPassEntity parkingPass, Integer companyId,
                                        Integer parkingLotId, Integer parkingSubLotId, String eventType) {

        ParkingEventEntity parkingEvent = new ParkingEventEntity();
        Integer parkingId = parkingPass.getParkingPassMaster().getParking().getId();
        parkingEvent.setCompanyId(companyId);
        parkingEvent.setParkingId(parkingId);
        parkingEvent.setParkingLotId(parkingLotId);
        parkingEvent.setParkingSubLotId(parkingSubLotId);
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
        parkingEvent.setShiftNumber("");

        parkingEventDAO.saveOrUpdateParkingEvent(parkingEvent);

        if (parkingPass.getIsPaid() == 1) {
            ParkingEventEntity passPaidEvent = new ParkingEventEntity();
            passPaidEvent.copy(parkingEvent);
            passPaidEvent.setEventType("PASS_PAID");
            passPaidEvent.setSerialNumber("" + parkingPass.getId() + day + hour + "P_P");
            passPaidEvent.setCost(new BigDecimal(parkingPass.getCost()));
            passPaidEvent.setParkingSubLotId(parkingEvent.getParkingSubLotId());
            passPaidEvent.setParkingPass(parkingPass);
            passPaidEvent.setUpdatedTime(DateTime.now());
            parkingEventDAO.saveOrUpdateParkingEvent(passPaidEvent);
        }
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
