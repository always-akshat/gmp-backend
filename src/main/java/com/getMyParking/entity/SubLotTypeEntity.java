package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by rahulgupta.s on 13/09/15.
 */
@Entity
@Table(name = "sub_lot_type", schema = "", catalog = "get_my_parking_v3")
public class SubLotTypeEntity {
    private String typeName;

    @Id
    @Column(name = "type_name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubLotTypeEntity that = (SubLotTypeEntity) o;

        return !(typeName != null ? !typeName.equals(that.typeName) : that.typeName != null);

    }

    @Override
    public int hashCode() {
        return typeName != null ? typeName.hashCode() : 0;
    }
}
