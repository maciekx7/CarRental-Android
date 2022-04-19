package com.wpam.carrental.model;

import com.google.gson.annotations.SerializedName;

public class CarCreation {
    int cost;
    long VIN;
    boolean availability;
    @SerializedName("CarModelId")
    int carModelId;

    public CarCreation(int cost, long VIN, boolean availability, int carModelId) {
        this.cost = cost;
        this.VIN = VIN;
        this.availability = availability;
        this.carModelId = carModelId;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setVIN(long VIN) {
        this.VIN = VIN;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setCarModelId(int carModelId) {
        this.carModelId = carModelId;
    }

    public int getCost() {
        return cost;
    }

    public long getVIN() {
        return VIN;
    }

    public boolean isAvailable() {
        return availability;
    }

    public int getCarModelId() {
        return carModelId;
    }

    @Override
    public String toString() {
        return "AbstractCar{" +
                "cost=" + cost +
                ", VIN=" + VIN +
                ", availability=" + availability +
                ", carModelId=" + carModelId +
                '}';
    }
}
