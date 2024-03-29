package com.example.joao.cafeteria_client_app.API;

import android.util.Log;

import org.json.*;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.Authentication.PastTransactionAuthActivity;
import com.example.joao.cafeteria_client_app.Authentication.RegisterActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.CartProduct;
import com.example.joao.cafeteria_client_app.Cafeteria.PastTransactionsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.PastTransactionsAdapter;
import com.example.joao.cafeteria_client_app.Cafeteria.Product;
import com.example.joao.cafeteria_client_app.Cafeteria.ProductsList;
import com.example.joao.cafeteria_client_app.Cafeteria.SettingsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.Transaction;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.Cafeteria.ProductsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.Voucher;
import com.example.joao.cafeteria_client_app.Cafeteria.VoucherActivity;
import com.loopj.android.http.*;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

import static com.example.joao.cafeteria_client_app.R.string.register;

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

    public static void pastTransactionAUTH (final PastTransactionAuthActivity pastTransactionsAuthActivity, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("pastTransactionAUTH/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");

                        pastTransactionsAuthActivity.onPastTransactionsAuthCompleted();

                    } else {
                        pastTransactionsAuthActivity.onPastTransactionAuthFailed(response.getString("message"));
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


    public static void updateCard(final SettingsActivity settings, RequestParams params) throws JSONException {

        CafeteriaRestClient.post("updateCreditCard/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");
                        Log.i("resultJSON",data.toString());
                        settings.onCreditCardChangeCompleted(data.getInt("creditCardID"));

                    } else {
                        settings.onCreditCardChangeFailed(code, response.getString("message"));
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

    public static void getTransactions(final PastTransactionsActivity pastTransactionsActivity) throws JSONException {

        CafeteriaRestClient.get("transactions/" + User.getInstance().getID().toString(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    List<Transaction> transactions = new ArrayList<>();

                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONArray data = response.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject transaction = data.getJSONObject(i);

                            // get transaction id
                            int transaction_id = transaction.getInt("id");

                            // get transaction date
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = format.parse(transaction.getString("date"));

                            // get transaction products
                            JSONArray products = transaction.getJSONArray("products");

                            List<CartProduct> lp = new ArrayList<>();

                            for (int j = 0; j < products.length(); j++) {
                                JSONObject product = products.getJSONObject(j);

                                int product_id = product.getInt("productid"), product_amount = product.getInt("amount");
                                Product p = ProductsList.getInstance().getProductByID(product_id);

                                lp.add(new CartProduct(p, product_amount));
                            }

                            transactions.add(new Transaction(transaction_id, date, lp));
                        }
                    } else {

                    }

                    pastTransactionsActivity.onGetPastTransactionsCompleted(transactions);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                pastTransactionsActivity.onGetPastTransactionsError(throwable);
            }
        });
    }

    public static void getVouchers(final VoucherActivity voucherActivity) throws JSONException {

        CafeteriaRestClient.get("vouchers/" + User.getInstance().getID().toString(), null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            List<Voucher> vouchers = new ArrayList<>();

                            int code = response.getInt("code");

                            if (code == 200) {

                                JSONObject data = response.getJSONObject("data");
                                JSONArray vouchersArray = data.getJSONArray("vouchers");

                                for (int i = 0; i < vouchersArray.length(); i++) {
                                    JSONObject voucher = vouchersArray.getJSONObject(i);

                                    // get voucher id
                                    int voucher_id = voucher.getInt("id");
                                    Log.i("",String.valueOf(voucher_id));

                                    // get voucher type
                                    String voucher_type;
                                    if (voucher.getInt("type") == 1) {
                                        voucher_type = "Free Popcorn";
                                    } else if (voucher.getInt("type") == 2) {
                                        voucher_type = "Free Coffee";
                                    } else {
                                        voucher_type = "Discount";
                                    }
                                    Log.i("",voucher_type);
                                    // get voucher serial
                                    int voucher_serial = voucher.getInt("serialnumber");
                                    Log.i("", String.valueOf(voucher_serial));
                                    // get voucher signature
                                    String voucher_signature = voucher.getString("signature");
                                    Log.i("",voucher_signature);

                                    vouchers.add(new Voucher(voucher_id, voucher_type, voucher_serial, voucher_signature));
                                }
                            } else {

                            }

                            voucherActivity.onGetVouchersCompleted(vouchers);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                        voucherActivity.onGetVouchersError(throwable);
                    }
                }

        );
    }

    public static User createUser(JSONObject data) throws JSONException {
        JSONObject userJSON = data.getJSONObject("user");

        JSONObject creditCardJSON = data.getJSONObject("creditCard");

        User user = new User(UUID.fromString(userJSON.getString("id")), userJSON.getString("name"), userJSON.getString("username"), userJSON.getString("email"), userJSON.getString("hash_pin"));
        user.createCreditCard(creditCardJSON.getInt("id"), creditCardJSON.getString("cardnumber"), creditCardJSON.getString("expmonth"), creditCardJSON.getString("expyear"));

        return user;
    }


}
