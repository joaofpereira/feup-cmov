package com.example.joao.cafeteriaterminal.API;


import android.util.Log;
import android.util.Pair;

import org.json.*;

import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.example.joao.cafeteriaterminal.Cafeteria.Voucher;
import com.example.joao.cafeteriaterminal.Cafeteria.VouchersList;
import com.example.joao.cafeteriaterminal.QRCODE.ReaderActivity;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class CafeteriaRestTerminalUsage {

    public static void confirmTransaction(final ReaderActivity readerActivity, RequestParams params) throws JSONException {

        CafeteriaRestTerminal.post("transaction/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");

                        Log.i("DATA: ", data.toString());

                        try {
                            JSONObject simple_voucher = data.getJSONObject("simple-voucher");

                            int id = simple_voucher.getInt("id"), type = simple_voucher.getInt("type"), serialNumber = simple_voucher.getInt("serialnumber");
                            String signature = simple_voucher.getString("signature");

                            VouchersList.getInstance().add(new Voucher(id, type, serialNumber, signature));
                        } catch (JSONException e) {
                             e.printStackTrace();
                            Log.i("RESULTADO SVoucher: ", "nulo");
                        }

                        try {
                            JSONObject discount_voucher = data.getJSONObject("discount-voucher");

                            int id = discount_voucher.getInt("id"), type = discount_voucher.getInt("type"), serialNumber = discount_voucher.getInt("serialnumber");
                            String signature = discount_voucher.getString("signature");

                            VouchersList.getInstance().add(new Voucher(id, type, serialNumber, signature));
                        } catch (JSONException e) {
                            Log.i("RESULTADO DVoucher: ", "nulo");
                        }

                        readerActivity.onTransactionRegisterComplete();

                    } else
                        Log.i("","failed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("ERROR: ", responseString + "\nStatusCode: " + statusCode + "\n");
            }
        });
    }

    public static void postTransactions(final ReaderActivity readerActivity, RequestParams params) throws JSONException {

        CafeteriaRestTerminal.post("updateTransactions/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");

                        Log.i("MENSAGEM DE SUCESSO: ", response.toString());

                        readerActivity.onUpdateTransactionsComplete();

                    } else
                        Log.i("","failed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.i("ERROR: ", "\nStatusCode: " + statusCode + "\n");
            }
        });
    }

    public static Transaction createTransaction(String data) throws JSONException {
        String lines[] = data.split("\\r?\\n");

        //JSONArray products = new JSONArray();
        List<Pair<Integer, Integer>> products = new ArrayList<>();

        for (int i = 2 ; i < lines.length ; i++){
            String temp[] = lines[i].split(":");

            Pair<Integer, Integer> pair = new Pair<>(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));

            products.add(pair);
            /*JSONObject obj = new JSONObject();
            obj.put("product-id", Integer.parseInt(temp[0]));
            obj.put("product-amount", Integer.parseInt(temp[1]));

            products.put(obj);*/
        }

        Transaction transaction = new Transaction(UUID.fromString(lines[0]), Float.parseFloat(lines[1]), products);

        return transaction;
    }
}
