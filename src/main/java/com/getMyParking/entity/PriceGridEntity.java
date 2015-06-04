package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
@Entity
@Table(name = "price_grid", schema = "", catalog = "get_my_parking")
public class PriceGridEntity {
    private int id;
    @NotNull
    private String priceStructure;
    @NotNull
    private int cost;
    @NotNull
    private Integer duration;
    @NotNull
    private int sequenceNumber;
    @JsonIgnore
    private PricingSlotEntity pricingSlotByPricingId;

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
    @Column(name = "duration", nullable = false, insertable = true, updatable = true, length = 45)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
        if (id != that.id) return false;
        if (sequenceNumber != that.sequenceNumber) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (priceStructure != null ? !priceStructure.equals(that.priceStructure) : that.priceStructure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (priceStructure != null ? priceStructure.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + sequenceNumber;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "pricing_id", referencedColumnName = "id", nullable = false)
    public PricingSlotEntity getPricingSlotByPricingId() {
        return pricingSlotByPricingId;
    }

    public void setPricingSlotByPricingId(PricingSlotEntity pricingSlotByPricingId) {
        this.pricingSlotByPricingId = pricingSlotByPricingId;
    }
}
