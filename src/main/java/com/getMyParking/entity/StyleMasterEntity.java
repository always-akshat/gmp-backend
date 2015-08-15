package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "style_master", schema = "", catalog = "get_my_parking_v2")
public class StyleMasterEntity {
    private Integer id;
    @JsonIgnore
    private Collection<ReceiptContentEntity> receiptContentsById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StyleMasterEntity that = (StyleMasterEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @OneToMany(mappedBy = "styleMaster")
    public Collection<ReceiptContentEntity> getReceiptContentsById() {
        return receiptContentsById;
    }

    public void setReceiptContentsById(Collection<ReceiptContentEntity> receiptContentsById) {
        this.receiptContentsById = receiptContentsById;
    }
}
