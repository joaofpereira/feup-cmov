package com.example.joao.cafeteriaterminal.Cafeteria;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransactionsList implements Serializable {

    private static TransactionsList instance = null;

    private List<Transaction> transactions;

    protected TransactionsList() {
        this.transactions = new ArrayList<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void add(Transaction t) {
        Log.i("Transaction content:", t.toString() );
        this.transactions.add(t);
        Log.i("Transaction content:", t.toString() );
    }

    public void remove(int index) {
        this.transactions.remove(index);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public int getSize() {
        return this.transactions.size();
    }

    public Transaction get(int index) {
        return this.transactions.get(index);
    }

    public static TransactionsList getInstance() {
        if(instance == null) {
            instance = new TransactionsList();
        }
        return instance;
    }

    public String toString() {
        String result = new String();

        for (Transaction t : transactions)
            result += t.toString();

        return result;
    }
}
