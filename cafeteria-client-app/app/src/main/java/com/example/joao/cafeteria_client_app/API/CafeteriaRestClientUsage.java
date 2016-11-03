package com.example.joao.cafeteria_client_app.API;

import android.util.Log;

import org.json.*;

import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.Authentication.RegisterActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.Product;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.Cafeteria.ProductsActivity;
import com.loopj.android.http.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class CafeteriaRestClientUsage {

    public static void login(final LoginActivity login, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("login/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");
                        User user = createUser(data);
                        String pin = data.getString("pin");

                        login.onLoginCompleted(pin, user);

                    } else {
                        login.onLoginFailed(response.getString("message"));
                    }

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

    public static void register(final RegisterActivity register, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("register/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");
                        User user = createUser(data);
                        String pin = data.getString("pin");

                        register.onRegisterCompleted(pin, user);

                    } else
                        register.onRegisterFailed(code, response.getString("message"));

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

    public static void getProducts(final ProductsActivity productsActivity) throws JSONException {

        CafeteriaRestClient.get("products", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray productsJSON = response.getJSONObject("data").getJSONArray("products");

                    List<Product> products = new ArrayList<Product>();

                    for (int i = 0; i < productsJSON.length(); i++) {
                        JSONObject product = productsJSON.getJSONObject(i);

                        int id = product.getInt("id");
                        String name = product.getString("name");
                        float price = Float.parseFloat(product.getString("price"));

                        Product p = new Product(id, name, price);

                        products.add(p);
                    }

                    productsActivity.onGetProductsCompleted(products);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                productsActivity.onGetProductsError(throwable);
            }
        });
    }

    public static User createUser(JSONObject data) throws JSONException {
        JSONObject userJSON = data.getJSONObject("user");

        JSONObject creditCardJSON = data.getJSONObject("creditCard");

        User user = new User(UUID.fromString(userJSON.getString("id")), userJSON.getString("name"), userJSON.getString("username"), userJSON.getString("email"), userJSON.getString("hash_pin"));
        user.createCreditCard(creditCardJSON.getInt("id"), creditCardJSON.getString("cardnumber"), creditCardJSON.getString("expmonth"), creditCardJSON.getString("expyear"));

        return user;
    }
}
