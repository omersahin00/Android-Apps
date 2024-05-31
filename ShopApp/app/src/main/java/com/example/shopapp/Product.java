package com.example.shopapp;

public class Product {
    private int index;
    private String name;
    private int price;
    private String brandName;
    private int imageResource;

    public Product() {

    }

    public Product(int index, String name, int price, String brandName, int imageResource) {
        this.index = index;
        this.name = name;
        this.price = price;
        this.brandName = brandName;
        this.imageResource = imageResource;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }
}
