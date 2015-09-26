package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 20/08/15.
 */
@Entity
@Table(name = "receipt_content", schema = "", catalog = "get_my_parking_v2")
public class ReceiptContentEntity {
    private Integer id;
    private String content;
    private Integer sequence;
    private String eventType;
    @JsonIgnore
    private ParkingSubLotEntity parkingSubLot;
    @JsonIgnore
    private ParkingPassMasterEntity parkingPassMaster;
    private String styleTitle;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content", nullable = false, insertable = true, updatable = true, length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "sequence", nullable = false, insertable = true, updatable = true)
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Basic
    @Column(name = "event_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Basic
    @Column(name = "style_master_title", nullable = false, insertable = true, updatable = true, length = 255)
    public String getStyleTitle() {
        return styleTitle;
    }

    public void setStyleTitle(String styleTitle) {
        this.styleTitle = styleTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptContentEntity)) return false;

        ReceiptContentEntity that = (ReceiptContentEntity) o;

        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (parkingPassMaster != null ? !parkingPassMaster.equals(that.parkingPassMaster) : that.parkingPassMaster != null)
            return false;
        if (parkingSubLot != null ? !parkingSubLot.equals(that.parkingSubLot) : that.parkingSubLot != null)
            return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;
        if (styleTitle != null ? !styleTitle.equals(that.styleTitle) : that.styleTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (parkingSubLot != null ? parkingSubLot.hashCode() : 0);
        result = 31 * result + (parkingPassMaster != null ? parkingPassMaster.hashCode() : 0);
        result = 31 * result + (styleTitle != null ? styleTitle.hashCode() : 0);
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

    @ManyToOne
    @JoinColumn(name = "parking_pass_master_id", referencedColumnName = "id", nullable = false)
    public ParkingPassMasterEntity getParkingPassMaster() {
        return parkingPassMaster;
    }

    public void setParkingPassMaster(ParkingPassMasterEntity parkingPassMaster) {
        this.parkingPassMaster = parkingPassMaster;
    }
}
