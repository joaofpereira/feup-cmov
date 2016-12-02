package com.example.joao.cafeteria_client_app.Cafeteria;

public class CartProduct {
    private int id;
    private String name;
    private float price;
    private int amount;

    public CartProduct(int id, String name, float price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public CartProduct(Product product, int amount) {
        this.id = product.getID();
        this.name = product.getName();
        this.price = product.getPrice();
        this.amount = amount;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
