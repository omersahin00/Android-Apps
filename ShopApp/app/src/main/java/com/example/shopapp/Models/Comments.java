package com.example.shopapp.Models;

public class Comments {
    private String userName;
    private String comment;
    private int point;
    private int productIndex;

    public Comments(String userName, String comment, int point, int productIndex) {
        this.userName = userName;
        this.comment = comment;
        this.point = point;
        this.productIndex = productIndex;
    }

    public Comments() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getProductIndex() {
        return productIndex;
    }

    public void setProductIndex(int productIndex) {
        this.productIndex = productIndex;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "userName='" + userName + '\'' +
                ", comment='" + comment + '\'' +
                ", point=" + point +
                ", productIndex=" + productIndex +
                '}';
    }
}
