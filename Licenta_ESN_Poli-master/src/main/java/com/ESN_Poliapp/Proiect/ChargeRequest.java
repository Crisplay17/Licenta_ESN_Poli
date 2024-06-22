package com.ESN_Poliapp.Proiect;


public class ChargeRequest {
    private String token;
    private double amount;
    private String currency;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Constructor, getteri și setteri

    public ChargeRequest(String token, double amount, String currency) {
        this.token = token;
        this.amount = amount;
        this.currency = currency;
    }
}


