package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by rahulgupta.s on 16/09/15.
 */
public class FocReasonsForParkingLotEntityPK implements Serializable {
    private String focReasonsReasonTitle;
    private int parkingSubLotId;

    @Column(name = "foc_reasons_reason_title", nullable = false, insertable = true, updatable = true, length = 255)
    @Id
    public String getTitle() {
        return focReasonsReasonTitle;
    }

    public void setTitle(String focReasonsReasonTitle) {
        this.focReasonsReasonTitle = focReasonsReasonTitle;
    }

    @Column(name = "parking_sub_lot_id", nullable = false, insertable = true, updatable = true)
    @Id
    public int getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(int parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FocReasonsForParkingLotEntityPK that = (FocReasonsForParkingLotEntityPK) o;

        if (parkingSubLotId != that.parkingSubLotId) return false;
        if (focReasonsReasonTitle != null ? !focReasonsReasonTitle.equals(that.focReasonsReasonTitle) : that.focReasonsReasonTitle != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = focReasonsReasonTitle != null ? focReasonsReasonTitle.hashCode() : 0;
        result = 31 * result + parkingSubLotId;
        return result;
    }
}
