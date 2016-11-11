package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Transaction implements Serializable {

    private UUID userID;
    private List<Pair<Integer, Integer>> products;
    //private JSONArray products;

    public Transaction(UUID userID, /*JSONArray products*/ List<Pair<Integer, Integer>> products) {
        this.userID = userID;
        this.products = products;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<Pair<Integer, Integer>> getProducts() {
        return products;
    }

    public String toString() {
        String result = new String();

        for (int i = 0; i < products.size(); i++)
            result += products.get(i).first + ":" + products.get(i).second + "\n";

        return "UserID: " + userID.toString() + "\nProducts: \n" + result;
    }
}

