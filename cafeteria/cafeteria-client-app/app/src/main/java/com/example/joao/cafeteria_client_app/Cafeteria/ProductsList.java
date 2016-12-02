package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.ArrayList;
import java.util.List;

public class ProductsList {
    private static ProductsList instance = null;

    private List<Product> products;

    public ProductsList() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getProductByID(int id) {
        for (Product p : products)
            if(p.getID() == id)
                return p;

        return null;
    }

    public static ProductsList getInstance() {
        if (instance == null) {
            instance = new ProductsList();
        }
        return instance;
    }
}
