package com.example.shopapp;

public class Account {
    private String userName;
    private String password;
    private int balance;

    public Account() {

    }

    public Account(String userName, String password, int balance) {
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userName='" + userName + '\'' +
                "password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
