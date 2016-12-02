package com.example.joao.cafeteriaterminal.Cafeteria;

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
        this.transactions.add(t);
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
