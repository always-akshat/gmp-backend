package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "price_grid", schema = "", catalog = "get_my_parking_v2")
public class PriceGridEntity {
    private int id;
    private String priceStructure;
    private int cost;
    private int duration;
    private int sequenceNumber;
    private PricingSlotEntity pricingSlot;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "price_structure", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPriceStructure() {
        return priceStructure;
    }

    public void setPriceStructure(String priceStructure) {
        this.priceStructure = priceStructure;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = true)
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "duration", nullable = false, insertable = true, updatable = true)
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "sequence_number", nullable = false, insertable = true, updatable = true)
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceGridEntity that = (PriceGridEntity) o;

        if (cost != that.cost) return false;
        if (duration != that.duration) return false;
        if (id != that.id) return false;
        if (sequenceNumber != that.sequenceNumber) return false;
        if (priceStructure != null ? !priceStructure.equals(that.priceStructure) : that.priceStructure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (priceStructure != null ? priceStructure.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + duration;
        result = 31 * result + sequenceNumber;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "pricing_id", referencedColumnName = "id", nullable = false)
    public PricingSlotEntity getPricingSlot() {
        return pricingSlot;
    }

    public void setPricingSlot(PricingSlotEntity pricingSlotByPricingId) {
        this.pricingSlot = pricingSlotByPricingId;
    }
}
