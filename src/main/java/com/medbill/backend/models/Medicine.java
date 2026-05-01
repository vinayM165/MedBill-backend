package com.medbill.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String genericName;

    private String manufacturer;

    private String batchNo;

    private LocalDate expiryDate;

    @Min(0)
    private Double purchasePrice;

    @Min(0)
    private Double mrp;

    @Min(0)
    private Integer stockQuantity;

    private String rackNumber;

    private String hsnCode;
    private String packSize;
    private Integer noOfStrips;
    private Integer tabletsPerStrip;
    private Integer totalTablets;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Medicine() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getRackNumber() {
        return rackNumber;
    }

    public void setRackNumber(String rackNumber) {
        this.rackNumber = rackNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public Integer getNoOfStrips() {
        return noOfStrips;
    }

    public void setNoOfStrips(Integer noOfStrips) {
        this.noOfStrips = noOfStrips;
    }

    public Integer getTabletsPerStrip() {
        return tabletsPerStrip;
    }

    public void setTabletsPerStrip(Integer tabletsPerStrip) {
        this.tabletsPerStrip = tabletsPerStrip;
    }

    public Integer getTotalTablets() {
        return totalTablets;
    }

    public void setTotalTablets(Integer totalTablets) {
        this.totalTablets = totalTablets;
    }


}
