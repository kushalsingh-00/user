package com.example.user.model;

import java.io.Serializable;

public class CartItem implements Serializable {
        public String name;
        public float qty;
        public int price;

        public CartItem(String name, int price) {
            this.name = name;
            this.price = price;
            qty=1;
        }

        public CartItem(String name, float qty, int price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
        }

    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
