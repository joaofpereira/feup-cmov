package com.example.joao.cafeteriaterminal.QRCODE;

import org.json.JSONObject;

public interface CallbackTransaction {
    public void onTransactionRegisterComplete(JSONObject response);

    public void onUpdateTransactionsComplete();
}
