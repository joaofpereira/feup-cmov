package com.example.joao.cafeteriaterminal.API;


import android.util.Log;
import android.util.Pair;

import org.json.*;

import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class CafeteriaRestTerminalUsage {

    public static void confirmTransaction( RequestParams params) throws JSONException {

        CafeteriaRestTerminal.post("transaction/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");


                        Log.i("","Success");

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




    public static Transaction createTransaction(String data) throws JSONException {


        String lines[] = data.split("\\r?\\n");

        List<Pair<Integer,Integer>> products_amount = new ArrayList<Pair<Integer,Integer>>();

        for (int i = 1 ; i < lines.length ; i++){

            String temp[] = lines[i].split(":");

            products_amount.add(new Pair(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));
        }




        Transaction transaction = new Transaction(UUID.fromString(lines[0]),products_amount);

        return transaction;
    }
}
