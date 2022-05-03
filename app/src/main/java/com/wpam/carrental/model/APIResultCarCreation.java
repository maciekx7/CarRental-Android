package com.wpam.carrental.model;

public class APIResultCarCreation extends APIResultMessageBasic{
    public Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public APIResultCarCreation(String message, Car car) {
        super(message);
        this.car = car;
    }
}
