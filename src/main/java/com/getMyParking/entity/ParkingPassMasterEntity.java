package com.getMyParking.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_pass_master", schema = "", catalog = "get_my_parking")
public class ParkingPassMasterEntity {
    private int id;
    private String name;
    private String passType;
    private int numbers;
    private int price;
    private String vehicleType;
    private String isActive;
    private Collection<ParkingPassEntity> parkingPassesById;
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
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "pass_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    @Basic
    @Column(name = "numbers", nullable = false, insertable = true, updatable = true)
    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Basic
    @Column(name = "vehicle_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic
    @Column(name = "isActive", nullable = false, insertable = true, updatable = true, length = 45)
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingPassMasterEntity that = (ParkingPassMasterEntity) o;

        if (id != that.id) return false;
        if (numbers != that.numbers) return false;
        if (price != that.price) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (passType != null ? !passType.equals(that.passType) : that.passType != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (passType != null ? passType.hashCode() : 0);
        result = 31 * result + numbers;
        result = 31 * result + price;
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "parkingPassMasterByParkingPassMasterId")
    public Collection<ParkingPassEntity> getParkingPassesById() {
        return parkingPassesById;
    }

    public void setParkingPassesById(Collection<ParkingPassEntity> parkingPassesById) {
        this.parkingPassesById = parkingPassesById;
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
