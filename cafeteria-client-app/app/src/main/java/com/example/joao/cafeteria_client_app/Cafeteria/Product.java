package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private float price;

    public Product(UUID id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }
}
