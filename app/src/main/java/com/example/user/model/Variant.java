package com.example.user.model;

public class Variant {
    public String name;
    public int price;

    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Rs. " + price;
    }
}
