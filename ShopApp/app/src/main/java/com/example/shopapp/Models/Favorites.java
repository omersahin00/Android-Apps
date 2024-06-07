package com.example.shopapp.Models;

public class Favorites {
    private String userName;
    private int productIndex;
    private String productKey;

    public Favorites(String userName, int productIndex) {
        this.userName = userName;
        this.productIndex = productIndex;
        this.productKey = userName + "_" + productIndex;
    }

    public Favorites() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.productKey = userName + "_" + this.productIndex;
    }

    public int getProductIndex() {
        return productIndex;
    }

    public void setProductIndex(int productIndex) {
        this.productIndex = productIndex;
        this.productKey = this.userName + "_" + productIndex;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "userName='" + userName + '\'' +
                ", productIndex=" + productIndex +
                ", productKey='" + productKey + '\'' +
                '}';
    }
}
