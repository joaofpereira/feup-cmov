package com.example.joao.cafeteriaterminal.Cafeteria;

import java.util.List;

public interface Callback {
    public void onGetPublicKeyCompleted(String publicKey);

    public void onGetProductsCompleted(List<Product> products);

    public void onTransactionRegisterComplete(TransactionTransmitted t);

    public void onUpdateTransactionsComplete();
}
