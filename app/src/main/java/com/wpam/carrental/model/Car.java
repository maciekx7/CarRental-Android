package com.wpam.carrental.model;

import com.google.gson.annotations.SerializedName;


public class Car extends CarCreation {
    int id;
    @SerializedName("CarModel")
    Model model;

    public Car(int id, int cost, long VIN, boolean availability, int carModelId, Model carModel) {
        super(cost, VIN, availability, carModelId);
        this.id = id;
        model = carModel;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setCarModel(Model carModel) {
        model = carModel;
    }

    public int getId() {
        return id;
    }



    public Model getCarModel() {
        return model;
    }

    @Override
    public String toString() {
        return "Car{" +
                "cost=" + cost +
                ", VIN=" + VIN +
                ", availability=" + availability +
                ", carModelId=" + carModelId +
                ", id=" + id +
                ", model=" + model +
                '}';
    }
}
