package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Pair;

import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private UUID userID;
    private Date date;
    private JSONArray products;

    public Transaction(UUID userID, JSONArray products) {
        this.userID = userID;
        this.date = new Date();
        this.products = products;
    }

    public UUID getUserID() {
        return userID;
    }

    public JSONArray getProducts() {
        return products;
    }

    public Date getDate() {
        return date;
    }
}

