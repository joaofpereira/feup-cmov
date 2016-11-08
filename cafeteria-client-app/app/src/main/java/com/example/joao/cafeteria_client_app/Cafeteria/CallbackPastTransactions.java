package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.Date;
import java.util.List;

public interface CallbackPastTransactions {
    public void onGetPastTransactionsCompleted(List<Transaction> transactions);

    /*public void onGetProductsError(Throwable throwable);*/
}
