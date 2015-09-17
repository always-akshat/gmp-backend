package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 16/09/15.
 */
@Entity
@Table(name = "foc_reasons_has_parking_lot", schema = "", catalog = "get_my_parking_v2")
public class FocReasonsForParkingLotEntity {
    @JsonIgnore
    private Integer id;
    private String title;
    @JsonIgnore
    private ParkingSubLotEntity parkingSubLot;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "foc_reasons_reason_title", nullable = false, insertable = true, updatable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String focReasonsReasonTitle) {
        this.title = focReasonsReasonTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FocReasonsForParkingLotEntity)) return false;
        FocReasonsForParkingLotEntity that = (FocReasonsForParkingLotEntity) o;
        if (parkingSubLot != null ? !parkingSubLot.equals(that.parkingSubLot) : that.parkingSubLot != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (parkingSubLot != null ? parkingSubLot.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLot() {
        return parkingSubLot;
    }

    public void setParkingSubLot(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLot = parkingSubLotByParkingSubLotId;
    }
}
