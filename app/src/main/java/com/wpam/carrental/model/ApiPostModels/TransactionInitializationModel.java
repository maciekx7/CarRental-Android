package com.wpam.carrental.model.ApiPostModels;

import com.google.gson.annotations.SerializedName;

public class TransactionInitializationModel {
    @SerializedName("CarId")
    int carId;
    String rentDate; //YYYY-MM-DD

    public TransactionInitializationModel(int carId, String rentDate) {
        this.carId = carId;
        this.rentDate = rentDate;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }
}
