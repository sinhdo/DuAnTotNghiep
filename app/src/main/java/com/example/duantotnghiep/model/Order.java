package com.example.duantotnghiep.model;

import java.util.List;

public class Order {
    private String id,idBuyer,idSeller,idProduct, nameProduct,imgProduct;

    private double amount;
    private int color;
    private Double total;
    private String date;
    private String address;
    private String numberPhone;
    private int quantity;
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private Boolean paid;
    private String status;
    private Boolean review;

    private List<String> reviewsList;

    public Order(List<String> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public List<String> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(List<String> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public Order(Boolean review) {
        this.review = review;
    }

    public Boolean getReview() {
        return review;
    }

    public void setReview(Boolean review) {
        this.review = review;
    }

    private List<Reviews> reviewList;

    // Constructor và các phương thức khác của Order

    // Phương thức để thiết lập danh sách đánh giá cho đơn hàng
    public void setReviewList(List<Reviews> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Reviews> getReviewList() {
        return reviewList;
    }

    public Order(String id, String idBuyer, String idSeller, String idProduct, String nameProduct, String imgProduct, int color, Double total, String date, String address, String numberPhone, int quantity, String notes, Boolean paid, String status) {
        this.id = id;
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.imgProduct = imgProduct;
        this.color = color;
        this.total = total;
        this.date = date;
        this.address = address;
        this.numberPhone = numberPhone;
        this.quantity = quantity;
        this.notes = notes;
        this.paid = paid;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(String idBuyer) {
        this.idBuyer = idBuyer;
    }

    public String getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order() {
    }


}
