package com.wpam.carrental.model;

import com.google.gson.annotations.SerializedName;
import com.wpam.carrental.model.enums.Body;
import com.wpam.carrental.model.enums.Fuel;

public class ModelCreation {

    String name;
    Fuel fuel;
    Body body;
    int productionYear;
    int enginePower;
    @SerializedName("MakeId")
    int makeId;

    public ModelCreation(String name, Fuel fuel, Body body, int productionYear, int enginePower, int makeId) {
        this.name = name;
        this.fuel = fuel;
        this.body = body;
        this.productionYear = productionYear;
        this.enginePower = enginePower;
        this.makeId = makeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    @Override
    public String toString() {
        return "ModelCreation{" +
                "name='" + name + '\'' +
                ", fuel=" + fuel +
                ", body=" + body +
                ", productionYear=" + productionYear +
                ", enginePower=" + enginePower +
                ", MakeId=" + makeId +
                '}';
    }
}

