package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "receipt_content", schema = "", catalog = "get_my_parking")
public class ReceiptContentEntity {
    private int id;
    private String content;
    private String ordering;
    private String placement;
    private ParkingLotEntity parkingLotByParkingLotId;

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
