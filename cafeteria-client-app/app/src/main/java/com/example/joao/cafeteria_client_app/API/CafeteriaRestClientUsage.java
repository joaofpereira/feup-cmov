package com.example.joao.cafeteria_client_app.API;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.*;

import com.example.joao.cafeteria_client_app.Authentication.User;
import com.loopj.android.http.*;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;


public class CafeteriaRestClientUsage {

    public static void register(final Activity activity, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("register/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONObject userJSON = data.getJSONObject("user");

                    User user = new User(UUID.fromString(userJSON.getString("id")), userJSON.getString("name"), userJSON.getString("username"), userJSON.getString("email"), userJSON.getString("hash_pin"), userJSON.getString("creditcardinfo"));
                    int pin = data.getInt("pin");

                    Log.i("", user.toString() + "\nPin: " + pin + "\n");
                    Log.i("uuid: ", user.getID().toString() + "\nPin: " + user.getHash_pin() + "\n");

                    SharedPreferences sp = activity.getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uuid", user.getID().toString());
                    editor.putString("hash_pin", user.getHash_pin());
                    editor.apply();

                    String sp_uuid = sp.getString("uuid", "");
                    String sp_hash_pin = sp.getString("hash_pin", "");

                    Log.i("sp_uuid: ", sp_uuid + "\nsp_hash_pin: " + sp_hash_pin + "\n");

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
