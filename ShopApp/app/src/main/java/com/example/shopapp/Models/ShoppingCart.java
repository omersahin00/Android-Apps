package com.example.shopapp.Models;

public class ShoppingCart {
    private String userName;
    private int productIndex;
    private String primaryKey;

    public ShoppingCart(String userName, int productIndex) {
        this.userName = userName;
        this.productIndex = productIndex;
        this.primaryKey = userName + "_" + productIndex;
    }

    public ShoppingCart() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.primaryKey = userName + "_" + this.productIndex;
    }

    public int getProductIndex() {
        return productIndex;
    }

    public void setProductIndex(int productIndex) {
        this.productIndex = productIndex;
        this.primaryKey = this.userName + "_" + productIndex;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "userName='" + userName + '\'' +
                ", productIndex=" + productIndex +
                ", primaryKey='" + primaryKey + '\'' +
                '}';
    }
}
