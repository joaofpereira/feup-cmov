package com.example.joao.cafeteriaterminal.Cafeteria;

import java.io.Serializable;
import java.util.List;

public class TransactionTransmitted implements Serializable {
    boolean popcorn, coffee, discount;
    List<ProductComplete> products;
    float transactionTotalPrice;
    int id;

    public TransactionTransmitted(boolean popcorn, boolean coffee, boolean discount, List<ProductComplete> products, float transactionTotalPrice, int transaction_id) {
        this.popcorn = popcorn;
        this.coffee = coffee;
        this.discount = discount;
        this.products = products;
        this.transactionTotalPrice = transactionTotalPrice;
        this.id = transaction_id;

        if(coffee)
            this.products.add(new ProductComplete("Café", 1, 0));
        if(popcorn)
            this.products.add(new ProductComplete("Pipocas", 1, 0));
    }

    public String getVouchersUsed() {
        if(coffee && popcorn && discount)
            return "Coffee, Popcorn, Discount";
        else if(coffee && popcorn)
            return "Coffee, Popcorn";
        else if(coffee && discount)
            return "Coffee, Discount";
        else if(discount && popcorn)
            return "Popcorn, Discount";
        else if(coffee)
            return "Coffee";
        else if(popcorn)
            return "Popcorn";
        else if(discount)
            return "Discount";
        else
            return "None";
    }

    public List<ProductComplete> getProducts() {
        return products;
    }

    public float getPreviouslyTotalPrice() {
        float total = 0;

        for(ProductComplete p : products)
            total += p.getTotalPrice();

        return total;
    }

    public int getId() {
        return id;
    }

    public float getTransactionTotalPrice() {
        return transactionTotalPrice;
    }
}
