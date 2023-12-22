package com.example.duantotnghiep.model;


public class ColorProduct {
    private int color = 0;
    private int price1 = 0;
    private int quantity1 = 0;
    private int price2 = 0;
    private int quantity2 = 0;
    private int price3 = 0;
    private int quantity3 = 0;
    private int price4 = 0;
    private int quantity4 = 0;
    private int price5 = 0;
    private int quantity5 = 0;
    public ColorProduct() {

    }
    public ColorProduct(int color) {
        this.color = color;
    }

    public ColorProduct(int color, int price1, int quantity1, int price2, int quantity2, int price3, int quantity3, int price4, int quantity4, int price5, int quantity5) {
        this.color = color;
        this.price1 = price1;
        this.quantity1 = quantity1;
        this.price2 = price2;
        this.quantity2 = quantity2;
        this.price3 = price3;
        this.quantity3 = quantity3;
        this.price4 = price4;
        this.quantity4 = quantity4;
        this.price5 = price5;
        this.quantity5 = quantity5;
    }

    @Override
    public String toString() {
        return "ColorProduct{" +
                "color=" + color +
                ", price1=" + price1 +
                ", quantity1=" + quantity1 +
                ", price2=" + price2 +
                ", quantity2=" + quantity2 +
                ", price3=" + price3 +
                ", quantity3=" + quantity3 +
                ", price4=" + price4 +
                ", quantity4=" + quantity4 +
                ", price5=" + price5 +
                ", quantity5=" + quantity5 +
                '}';
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPrice1() {
        return price1;
    }

    public void setPrice1(int price1) {
        this.price1 = price1;
    }

    public int getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(int quantity1) {
        this.quantity1 = quantity1;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public int getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(int quantity2) {
        this.quantity2 = quantity2;
    }

    public int getPrice3() {
        return price3;
    }

    public void setPrice3(int price3) {
        this.price3 = price3;
    }

    public int getQuantity3() {
        return quantity3;
    }

    public void setQuantity3(int quantity3) {
        this.quantity3 = quantity3;
    }

    public int getPrice4() {
        return price4;
    }

    public void setPrice4(int price4) {
        this.price4 = price4;
    }

    public int getQuantity4() {
        return quantity4;
    }

    public void setQuantity4(int quantity4) {
        this.quantity4 = quantity4;
    }

    public int getPrice5() {
        return price5;
    }

    public void setPrice5(int price5) {
        this.price5 = price5;
    }

    public int getQuantity5() {
        return quantity5;
    }

    public void setQuantity5(int quantity5) {
        this.quantity5 = quantity5;
    }
}
