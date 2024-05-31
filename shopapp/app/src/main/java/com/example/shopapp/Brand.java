package com.example.shopapp;

public class Brand {
    private String name;
    private int imageResource;

    public Brand() {

    }

    public Brand(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }
}
