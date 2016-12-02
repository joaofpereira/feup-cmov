package com.example.joao.cafeteriaterminal.Cafeteria;

import java.io.Serializable;

public class ProductComplete implements Serializable {
    float totalPrice;
    String name;
    int amount;

    public ProductComplete(String name, int amount, float totalPrice) {
        this.name = name;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public float getTotalPrice() {
        return totalPrice;
    }
}
