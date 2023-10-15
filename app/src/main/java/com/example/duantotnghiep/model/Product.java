package com.example.duantotnghiep.model;

public class Product {
    private String id,sellerId,name,color,size,price,categoryID,brand,description,reviewID;
    private String imgProduct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public Product() {
    }

    public Product(String id, String sellerId, String name, String color, String size, String price, String categoryID, String brand, String description, String reviewID, String imgProduct) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.color = color;
        this.size = size;
        this.price = price;
        this.categoryID = categoryID;
        this.brand = brand;
        this.description = description;
        this.reviewID = reviewID;
        this.imgProduct = imgProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }
}
