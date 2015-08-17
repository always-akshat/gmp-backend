package com.getMyParking.entity;

import java.util.List;

/**
 * Created by rahulgupta.s on 17/08/15.
 */
public class GetParkingEventResponse {

    private Integer parkingSubLotId;
    private List<ParkingEventEntity> parkingEvents;

    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    public List<ParkingEventEntity> getParkingEvents() {
        return parkingEvents;
    }

    public void setParkingEvents(List<ParkingEventEntity> parkingEvents) {
        this.parkingEvents = parkingEvents;
    }
}
