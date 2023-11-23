package com.example.duantotnghiep.model;

public class Discount  {
    private String id;
    private String code;
    private double amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Discount(){

    }

    public Discount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Discount(String id, String code, double amount) {
        this.id = id;
        this.code = code;
        this.amount = amount;
    }

    public Discount(String code, double amount) {
        this.code = code;
        this.amount = amount;
    }
}
