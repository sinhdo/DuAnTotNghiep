package com.example.duantotnghiep.model;

public class AddProductToCart {
    private String id;
    private String id_user;
    private String id_product;
    private String name_product;
    private Integer color_product;
    private String size_product;
    private String image_product;
    private int quantity_product;
    private double pricetotal_product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public Integer getColor_product() {
        return color_product;
    }

    public void setColor_product(Integer color_product) {
        this.color_product = color_product;
    }

    public String getSize_product() {
        return size_product;
    }

    public void setSize_product(String size_product) {
        this.size_product = size_product;
    }

    public String getImage_product() {
        return image_product;
    }

    public void setImage_product(String image_product) {
        this.image_product = image_product;
    }

    public int getQuantity_product() {
        return quantity_product;
    }

    public void setQuantity_product(int quantity_product) {
        this.quantity_product = quantity_product;
    }

    public double getPricetotal_product() {
        return pricetotal_product;
    }

    public void setPricetotal_product(double pricetotal_product) {
        this.pricetotal_product = pricetotal_product;
    }

    public AddProductToCart() {
    }

    public AddProductToCart(String id, String id_user, String id_product, String name_product, Integer color_product, String size_product, String image_product, int num_product, double pricetotal_product) {
        this.id = id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.name_product = name_product;
        this.color_product = color_product;
        this.size_product = size_product;
        this.image_product = image_product;
        this.quantity_product = num_product;
        this.pricetotal_product = pricetotal_product;
    }
}
