package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Transaction implements Serializable {

    private UUID userID;
    private float totalValue;
    private List<Pair<Integer, Integer>> products;
    private List<TransactionVoucher> vouchers;

    public Transaction(UUID userID, float totalValue, List<Pair<Integer, Integer>> products, List<TransactionVoucher> vouchers) {
        this.userID = userID;
        this.totalValue = totalValue;
        this.products = products;
        this.vouchers = vouchers;
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

    public List<TransactionVoucher> getVouchers() {
        return vouchers;
    }

    public String toString() {
        String productsS = new String(), vouchersS = new String();

        for (int i = 0; i < vouchers.size(); i++)
            vouchersS += vouchers.get(i).toString();

        for (int i = 0; i < products.size(); i++)
            productsS += products.get(i).first + ":" + products.get(i).second + "\n";

        return "UserID: " + userID.toString() + "\nTotalValue: " + this.totalValue + "\nVouchers: \n" + vouchersS + "\nProducts: \n" + productsS;
    }
}

