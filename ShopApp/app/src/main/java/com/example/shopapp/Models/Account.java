package com.example.shopapp.Models;

public class Account {
    private String name;
    private String password;
    private int balance;

    public Account() {

    }

    public Account(String name, String password, int balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "userName='" + name + '\'' +
                "password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
