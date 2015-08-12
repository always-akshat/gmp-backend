package com.getMyParking.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "style_master", schema = "", catalog = "get_my_parking_v2")
public class StyleMasterEntity {
    private int id;
    private Collection<ReceiptContentEntity> receiptContentsById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @OneToMany(mappedBy = "styleMasterByStyleId")
    public Collection<ReceiptContentEntity> getReceiptContentsById() {
        return receiptContentsById;
    }

    public void setReceiptContentsById(Collection<ReceiptContentEntity> receiptContentsById) {
        this.receiptContentsById = receiptContentsById;
    }
}
