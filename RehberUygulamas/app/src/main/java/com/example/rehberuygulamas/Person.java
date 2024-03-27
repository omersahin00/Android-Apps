package com.example.rehberuygulamas;

public class Person {
    private int id;
    private String name;
    private String phone;

    public Person() {
    }

    public Person(String name, String telefon) {
        this.name = name;
        this.phone = telefon;
    }

    public Person(int id, String name, String telefon) {
        this.id = id;
        this.name = name;
        this.phone = telefon;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telefon='" + phone + '\'' +
                '}';
    }
}
