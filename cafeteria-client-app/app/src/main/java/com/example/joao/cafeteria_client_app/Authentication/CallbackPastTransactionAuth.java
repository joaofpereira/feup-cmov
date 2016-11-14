package com.example.joao.cafeteria_client_app.Authentication;

import com.example.joao.cafeteria_client_app.Cafeteria.User;

/**
 * Created by josec on 13/11/2016.
 */
public interface CallbackPastTransactionAuth {
    public void onPastTransactionsAuthCompleted();

    public void onPastTransactionAuthFailed(String msg);
}
