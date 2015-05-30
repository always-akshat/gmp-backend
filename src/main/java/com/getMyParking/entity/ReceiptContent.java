package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
@Entity
@Table(name = "receipt_content", schema = "", catalog = "get_my_parking")
public class ReceiptContent {
    private int id;
    private String content;
    private String ordering;
    private String placement;
    private int parkingLotId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Basic
    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptContent that = (ReceiptContent) o;

        if (id != that.id) return false;
        if (parkingLotId != that.parkingLotId) return false;
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
        result = 31 * result + parkingLotId;
        return result;
    }
}
