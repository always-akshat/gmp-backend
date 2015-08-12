package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "receipt_content", schema = "", catalog = "get_my_parking_v2")
public class ReceiptContentEntity {
    private int id;
    private String content;
    private int sequence;
    private String eventType;
    private int parkingSubLotId;
    private int styleId;
    private ParkingSubLotEntity parkingSubLotByParkingSubLotId;
    private StyleMasterEntity styleMasterByStyleId;

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
    @Column(name = "parking_sub_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(int parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    @Basic
    @Column(name = "style_id", nullable = false, insertable = true, updatable = true)
    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptContentEntity that = (ReceiptContentEntity) o;

        if (id != that.id) return false;
        if (parkingSubLotId != that.parkingSubLotId) return false;
        if (sequence != that.sequence) return false;
        if (styleId != that.styleId) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + sequence;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + parkingSubLotId;
        result = 31 * result + styleId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLotByParkingSubLotId() {
        return parkingSubLotByParkingSubLotId;
    }

    public void setParkingSubLotByParkingSubLotId(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLotByParkingSubLotId = parkingSubLotByParkingSubLotId;
    }

    @ManyToOne
    @JoinColumn(name = "style_id", referencedColumnName = "id", nullable = false)
    public StyleMasterEntity getStyleMasterByStyleId() {
        return styleMasterByStyleId;
    }

    public void setStyleMasterByStyleId(StyleMasterEntity styleMasterByStyleId) {
        this.styleMasterByStyleId = styleMasterByStyleId;
    }
}
