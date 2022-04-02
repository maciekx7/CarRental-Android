package com.wpam.carrental.model;

import com.google.gson.annotations.SerializedName;
import com.wpam.carrental.model.enums.Body;
import com.wpam.carrental.model.enums.Fuel;

public class Model extends ModelCreation {
    int id;

    @SerializedName("Make")
    Make make;

    public Model(int id, String name, Fuel fuel, Body body, int productionYear, int enginePower, int makeId, Make make) {
        super(name, fuel, body, productionYear, enginePower, makeId);
        this.id = id;
        this.make = make;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", make=" + make +
                ", name='" + name + '\'' +
                ", fuel=" + fuel +
                ", body=" + body +
                ", productionYear=" + productionYear +
                ", enginePower=" + enginePower +
                ", MakeId=" + makeId +
                '}';
    }
}
