package com.example.joao.cafeteria_client_app.Cafeteria;

/**
 * Created by josec on 13/11/2016.
 */
public interface CallbackSettings{
    public void onCreditCardChangeCompleted(int creditCardID);

    public void onCreditCardChangeFailed(int code, String msg);
}
