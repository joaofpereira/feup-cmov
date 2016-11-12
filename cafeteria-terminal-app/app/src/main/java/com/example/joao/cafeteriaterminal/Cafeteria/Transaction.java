package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Transaction implements Serializable {

    private UUID userID;
    private float totalValue;
    private List<Pair<Integer, Integer>> products;

    public Transaction(UUID userID, float totalValue, List<Pair<Integer, Integer>> products) {
        this.userID = userID;
        this.totalValue = totalValue;
        this.products = products;
    }

    public UUID getUserID() {
        return userID;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public List<Pair<Integer, Integer>> getProducts() {
        return products;
    }

    public String toString() {
        String result = new String();

        for (int i = 0; i < products.size(); i++)
            result += products.get(i).first + ":" + products.get(i).second + "\n";

        return "UserID: " + userID.toString() + "\nTotalValue: " + this.totalValue + "\nProducts: \n" + result;
    }
}

