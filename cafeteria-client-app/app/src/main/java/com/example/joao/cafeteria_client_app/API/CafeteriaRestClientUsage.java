package com.example.joao.cafeteria_client_app.API;

import android.util.Log;

import org.json.*;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

public class CafeteriaRestClientUsage {

    public static void getProduct(String name) throws JSONException {
        CafeteriaRestClient.get("product/" + name, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    String name = data.getString("name");

                    Log.i("", "Name: of product:" + name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
