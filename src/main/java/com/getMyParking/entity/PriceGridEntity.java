package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "price_grid", schema = "", catalog = "get_my_parking")
public class PriceGridEntity {

    private int id;
    @NotNull
    private String priceStructure;
    private Integer startHour;
    private Integer endHour;
    @NotNull
    private int cost;
    private String slabHour;
    @JsonIgnore
    private PricingSlotEntity pricingSlotByPricingId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
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
    @Column(name = "start_hour", nullable = true, insertable = true, updatable = true)
    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    @Basic
    @Column(name = "end_hour", nullable = true, insertable = true, updatable = true)
    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
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
    @Column(name = "slab_hour", nullable = true, insertable = true, updatable = true, length = 45)
    public String getSlabHour() {
        return slabHour;
    }

    public void setSlabHour(String slabHour) {
        this.slabHour = slabHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceGridEntity that = (PriceGridEntity) o;

        if (cost != that.cost) return false;
        if (id != that.id) return false;
        if (endHour != null ? !endHour.equals(that.endHour) : that.endHour != null) return false;
        if (priceStructure != null ? !priceStructure.equals(that.priceStructure) : that.priceStructure != null)
            return false;
        if (slabHour != null ? !slabHour.equals(that.slabHour) : that.slabHour != null) return false;
        if (startHour != null ? !startHour.equals(that.startHour) : that.startHour != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (priceStructure != null ? priceStructure.hashCode() : 0);
        result = 31 * result + (startHour != null ? startHour.hashCode() : 0);
        result = 31 * result + (endHour != null ? endHour.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + (slabHour != null ? slabHour.hashCode() : 0);
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
