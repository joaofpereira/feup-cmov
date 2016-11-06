package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Pair;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by josec on 02/11/2016.
 */

public class Transaction {


    private UUID userID;
    private Date date;
    private List<Pair<Integer,Integer>>productIDList;

    public Transaction(UUID userID, List<Pair<Integer,Integer>>productIDList) {

        this.userID = userID;
        this.date = new Date();
        this.productIDList = productIDList;

    }


    public UUID getUserID() {
        return userID;
    }


    public List<Pair<Integer, Integer>> getProductIDList() {
        return productIDList;
    }

    public Date getDate() {
        return date;
    }

}

