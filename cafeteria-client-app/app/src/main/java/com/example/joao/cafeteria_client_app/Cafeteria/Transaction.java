package com.example.joao.cafeteria_client_app.Cafeteria;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Transaction implements Serializable{
    private int id;
    private Date date;

    List<CartProduct> products;

    public Transaction(int id, Date date, List<CartProduct> products) {
        this.id = id;
        this.date = date;
        this.products = products;
    }

    public int getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public List<CartProduct> getProducts() {
        return this.products;
    }

    public int getTotalProducts() {
        int total = 0;

        for (CartProduct cp : products) {
            total += cp.getAmount();
        }

        return total;
    }

    public float getTotalPrice() {
        float total = 0;

        for (CartProduct cp : products) {
            total += cp.getPrice() * cp.getAmount();
        }

        return total;
    }
}
