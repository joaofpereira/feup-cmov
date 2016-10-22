package com.example.joao.cafeteria_client_app.API;

import org.json.*;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

public class CafeteriaRestClientUsage {

    public void getProduct(String name) throws JSONException {
        CafeteriaRestClient.get("product/" + name, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String name = response.getString("name");
                    System.out.println(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
