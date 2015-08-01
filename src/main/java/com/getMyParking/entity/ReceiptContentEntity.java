package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "receipt_content", schema = "", catalog = "get_my_parking")
public class ReceiptContentEntity {

    private Integer id;
    @NotNull
    private String content;
    @NotNull
    private String ordering;
    @NotNull
    private String placement;
    @NotNull
    private String vehicleType;
    private String receiptSection;
    private Integer sequence;
    private Integer flag;
    private String event;
    @JsonIgnore
    private ParkingLotEntity parkingLotByParkingLotId;

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
    @Column(name = "content", nullable = true, insertable = true, updatable = true, length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "receipt_section", nullable = true, insertable = true, updatable = true, length = 255)
    public String getReceiptSection() {
        return receiptSection;
    }

    public void setReceiptSection(String receiptSection) {
        this.receiptSection = receiptSection;
    }

    @Basic
    @Column(name = "sequence", nullable = true, insertable = true, updatable = true)
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Basic
    @Column(name = "flag", nullable = true, insertable = true, updatable = true)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Basic
    @Column(name = "event", nullable = true, insertable = true, updatable = true, length = 255)
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Basic
    @Column(name = "vehicle_type", nullable = true, insertable = true, updatable = true, length = 255)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic
    @Column(name = "ordering", nullable = true, insertable = true, updatable = true, length = 45)
    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    @Basic
    @Column(name = "placement", nullable = true, insertable = true, updatable = true, length = 45)
    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptContentEntity that = (ReceiptContentEntity) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (ordering != null ? !ordering.equals(that.ordering) : that.ordering != null) return false;
        if (placement != null ? !placement.equals(that.placement) : that.placement != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (ordering != null ? ordering.hashCode() : 0);
        result = 31 * result + (placement != null ? placement.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
    }
}
