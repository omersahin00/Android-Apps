package com.example.rehberuygulamas;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String email;

    public Person() {
    }

    public Person(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Person(int id, String name, String telefon, String email) {
        this.id = id;
        this.name = name;
        this.phone = telefon;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String telefon) {
        this.phone = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telefon='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
