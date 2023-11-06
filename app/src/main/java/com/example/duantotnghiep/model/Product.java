package com.example.duantotnghiep.model;

import java.util.List;

public class Product {
    private String id;
    private String sellerId;
    private String name;
    private ProductType productType;
    private String categoryID;
    private String brand;
    private String description;
    private List<String> imgProduct;
    private List<Integer> color;
    private int sold;
    private String reviewId;
    private int quantity;
    private double price;
    private List<String> size;
    private boolean paid;
    public enum ProductType {
        CLOTHING,
        FOOTWEAR
    }
    public Product(){

    }
    public Product(String name, double price, List<String> imgProduct, List<String> size, List<Integer> color, int quantity) {
        this.name = name;
        this.price = price;
        this.imgProduct = imgProduct;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
    }
    public Product(String id, String sellerId, String name, ProductType productType, String categoryID, String brand, String description, List<String> imgProduct, List<Integer> color, int sold, String reviewId, int quantity, double price, List<String> size) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.productType = productType;
        this.categoryID = categoryID;
        this.brand = brand;
        this.description = description;
        this.imgProduct = imgProduct;
        this.color = color;
        this.sold = sold;
        this.reviewId = reviewId;
        this.quantity = quantity;
        this.price = price;
        this.size = size;

    }


    public Product(String id, String name, ProductType size, String categoryID, String brand, String description, List<String> imgProduct, List<Integer> color, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.productType = size;
        this.categoryID = categoryID;
        this.brand = brand;
        this.description = description;
        this.imgProduct = imgProduct;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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

    public List<String> getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(List<String> imgProduct) {
        this.imgProduct = imgProduct;
    }

    public List<Integer> getColor() {
        return color;
    }

    public void setColor(List<Integer> color) {
        this.color = color;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}