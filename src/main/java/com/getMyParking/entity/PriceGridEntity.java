package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "price_grid", schema = "", catalog = "get_my_parking_v2")
public class PriceGridEntity {
    private Integer id;
    private String priceStructure;
    private Integer cost;
    private Integer duration;
    private Integer sequenceNumber;
    private PricingSlotEntity pricingSlot;

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
    @Column(name = "price_structure", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPriceStructure() {
        return priceStructure;
    }

    public void setPriceStructure(String priceStructure) {
        this.priceStructure = priceStructure;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = true)
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "duration", nullable = false, insertable = true, updatable = true)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "sequence_number", nullable = false, insertable = true, updatable = true)
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
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
