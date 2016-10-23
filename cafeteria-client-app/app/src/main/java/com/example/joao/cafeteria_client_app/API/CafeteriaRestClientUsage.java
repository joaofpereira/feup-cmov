package com.example.joao.cafeteria_client_app.API;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.*;

import com.example.joao.cafeteria_client_app.Authentication.RegisterActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.loopj.android.http.*;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;


public class CafeteriaRestClientUsage {

    public static void register(final RegisterActivity register, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("register/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONObject userJSON = data.getJSONObject("user");

                    User user = new User(UUID.fromString(userJSON.getString("id")), userJSON.getString("name"), userJSON.getString("username"), userJSON.getString("email"), userJSON.getString("hash_pin"), userJSON.getString("creditcardinfo"));
                    int pin = data.getInt("pin");

                    register.onRegisterCompleted(pin, user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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
