package com.wpam.carrental.model.ApiPostModels;

public class TransactionClosingModel {
    int cost;
    String returnDate; //YYYY-MM-DD

    public TransactionClosingModel(int cost, String returnDate) {
        this.cost = cost;
        this.returnDate = returnDate;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
