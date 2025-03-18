package com.javaweb.model;

public class BuildingDTO {
    private String name;
    private Integer numOfBasement;
    private String street;
    private String ward;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getNumOfBasement() {
        return numOfBasement;
    }
    public void setNumOfBasement(Integer numOfBasement) {
        this.numOfBasement = numOfBasement;
    }
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
}
