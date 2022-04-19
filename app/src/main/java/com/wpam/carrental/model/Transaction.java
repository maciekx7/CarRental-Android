package com.wpam.carrental.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    int id;
    int cost;
    String rentDate;
    String returnDate;
    @SerializedName("CarId")
    int carId;
    @SerializedName("UserId")
    int userId;
    @SerializedName("User")
    User user;
    @SerializedName("Car")
    Car car;

    public Transaction(int id, int cost, String rentDate, String returnDate, int carId, int userId, User user, Car car) {
        this.id = id;
        this.cost = cost;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.carId = carId;
        this.userId = userId;
        this.user = user;
        this.car = car;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public class User {
        String email;

        public User(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
