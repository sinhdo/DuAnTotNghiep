package com.example.duantotnghiep.model;

public class Card {
    private String cardSerial;
    private String cardPin;
    private String cardProvider;
    private String cardValue;
    private String time;
    private String username;
    private String userId; // Thêm trường userId

    public Card(String cardSerial, String cardPin, String cardProvider, String cardValue, String time, String username, String userId) {
        this.cardSerial = cardSerial;
        this.cardPin = cardPin;
        this.cardProvider = cardProvider;
        this.cardValue = cardValue;
        this.time = time;
        this.username = username;
        this.userId = userId; // Gán ID người dùng
    }

    // Các getter và setter


    public String getCardSerial() {
        return cardSerial;
    }

    public void setCardSerial(String cardSerial) {
        this.cardSerial = cardSerial;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public String getCardProvider() {
        return cardProvider;
    }

    public void setCardProvider(String cardProvider) {
        this.cardProvider = cardProvider;
    }

    public String getCardValue() {
        return cardValue;
    }

    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}



