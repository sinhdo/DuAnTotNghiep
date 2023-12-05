package com.example.duantotnghiep.model;

public class Reviews {


    private String userId;
    private String displayName;
    private String productId;
    private String comment;

    public Reviews() {
    }

    public Reviews(String userId, String displayName, String productId, String comment) {
        this.userId = userId;
        this.displayName = displayName;
        this.productId = productId;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //    private String userId;
//    private String productId;
//    private String comment;
//
//    private String noComent;
//
//    private String userName;
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public Reviews(String noComent) {
//        this.noComent = noComent;
//    }
//
//    public String getNoComent() {
//        return noComent;
//    }
//
//    public void setNoComent(String noComent) {
//        this.noComent = noComent;
//    }
//
//    public Reviews() {
//    }
//
//    public Reviews(String userId, String productId, String comment) {
//        this.userId = userId;
//        this.productId = productId;
//        this.comment = comment;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
}
