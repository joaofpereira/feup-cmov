package com.example.joao.cafeteriaterminal.Cafeteria;

import java.util.List;

public interface Callback {
    public void onGetPublicKeyCompleted(String publicKey);

    public void onGetProductsCompleted(List<Product> products);

    public void onTransactionRegisterComplete(TransactionTransmitted t);

    public void onUpdateTransactionsComplete();

    public void onGetBlacklistCompleted(List<BlackListUser> blacklist);

    public void onBlackListInserted(BlackListUser blu);

    public void onBlackListInsertedInUpdateTransactions(BlackListUser blu);

    public void onUserNotExists(String message);

    public void onUserNotExistsInUpdateTransactions();
}
