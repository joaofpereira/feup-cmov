package com.example.joao.cafeteriaterminal.API;


import android.util.Log;
import android.util.Pair;

import org.json.*;

import com.example.joao.cafeteriaterminal.Cafeteria.BlackListUser;
import com.example.joao.cafeteriaterminal.Cafeteria.Product;
import com.example.joao.cafeteriaterminal.Cafeteria.ProductComplete;
import com.example.joao.cafeteriaterminal.Cafeteria.ProductsList;
import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionTransmitted;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionVoucher;
import com.example.joao.cafeteriaterminal.QRCODE.ReaderActivity;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class CafeteriaRestTerminalUsage {

    public static void getPublicKey(final ReaderActivity readerActivity) throws JSONException {
        CafeteriaRestTerminal.get("publickey/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.i("RESPONSE:", response.toString());

                    String publicKey = response.getString("data");

                    Log.i("CHAVE LIDA: ", publicKey.toString());

                    readerActivity.onGetPublicKeyCompleted(publicKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getProducts(final ReaderActivity readerActivity) throws JSONException {

        CafeteriaRestTerminal.get("products/", null, new JsonHttpResponseHandler() {
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

                    readerActivity.onGetProductsCompleted(products);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                Log.i("ERRO SERVER: ", "Falhou getProducts : " + throwable.toString());
            }
        });
    }

    public static void getBlacklist(final ReaderActivity readerActivity) throws JSONException {

        CafeteriaRestTerminal.get("blacklist/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray blacklistJSON = response.getJSONObject("data").getJSONArray("blacklist-users");

                    List<BlackListUser> blacklist = new ArrayList<>();

                    for (int i = 0; i < blacklistJSON.length(); i++) {
                        JSONObject blacklistUser = blacklistJSON.getJSONObject(i);

                        int id = blacklistUser.getInt("id");
                        UUID userID = UUID.fromString(blacklistUser.getString("userid"));
                        String message = blacklistUser.getString("motive");

                        BlackListUser blu = new BlackListUser(id, userID, message);

                        blacklist.add(blu);
                    }

                    readerActivity.onGetBlacklistCompleted(blacklist);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                Log.i("ERRO SERVER: ", "Falhou getBlacklist : " + throwable.toString());
            }
        });
    }

    public static void confirmTransaction(final ReaderActivity readerActivity, RequestParams params) throws JSONException {

        CafeteriaRestTerminal.post("transaction/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");

                        Log.i("DATA: ", data.toString());

                        JSONObject vouchers_json = data.getJSONObject("vouchers-used");

                        boolean popcorn = vouchers_json.getBoolean("popcorn");
                        boolean coffee = vouchers_json.getBoolean("coffee");
                        boolean discount = vouchers_json.getBoolean("discount");

                        JSONArray products_json = data.getJSONArray("transaction-products");
                        List<ProductComplete> products = new ArrayList<>();

                        for(int i = 0; i < products_json.length(); i++) {
                            JSONObject product_json = products_json.getJSONObject(i);

                            int id_json = product_json.getInt("product-id");
                            int amount_json = product_json.getInt("product-amount");

                            Product p = ProductsList.getInstance().getProductByID(id_json);

                            ProductComplete pc = new ProductComplete(p.getName(), amount_json, p.getPrice() * amount_json);

                            products.add(pc);
                        }

                        float transaction_value = Float.parseFloat(data.getString("transaction-totalValue"));

                        int transaction_id = data.getInt("transaction-id");

                        TransactionTransmitted t = new TransactionTransmitted(popcorn, coffee, discount, products, transaction_value, transaction_id);

                        readerActivity.onTransactionRegisterComplete(t);

                    } else if(code == 405) {
                        Log.i("VOUCHERS: ", "nao existem");
                    } else if(code == 406 || code == 407) {
                        JSONObject data = response.getJSONObject("data");

                        int id = data.getInt("id");
                        UUID userID = UUID.fromString(data.getString("userid"));
                        String message = data.getString("motive");

                        BlackListUser blackListUser = new BlackListUser(id, userID, message);

                        readerActivity.onBlackListInserted(blackListUser);
                    }else
                        Log.i("ERRO: ", "");

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

        CafeteriaRestTerminal.post("transaction/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200) {
                        JSONObject data = response.getJSONObject("data");

                        Log.i("MENSAGEM DE SUCESSO: ", response.toString());

                        readerActivity.onUpdateTransactionsComplete();

                    }  else if(code == 406 || code == 407) {
                        JSONObject data = response.getJSONObject("data");

                        int id = data.getInt("id");
                        UUID userID = UUID.fromString(data.getString("userid"));
                        String message = data.getString("motive");

                        BlackListUser blackListUser = new BlackListUser(id, userID, message);

                        readerActivity.onBlackListInsertedInUpdateTransactions(blackListUser);
                    }else
                        Log.i("ERRO: ", "");

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

        List<TransactionVoucher> vouchers = new ArrayList<>();

        int current_line = 2;
        int numberOfVouchers = Integer.parseInt(lines[2]);

        for (int i = 0; i < numberOfVouchers; i++) {
            vouchers.add(new TransactionVoucher(Integer.parseInt(lines[current_line + 1]), Integer.parseInt(lines[current_line + 3]), lines[current_line + 4], Integer.parseInt(lines[current_line + 2])));

            current_line += 4;
        }

        current_line++;

        List<Pair<Integer, Integer>> products = new ArrayList<>();

        for (int i = current_line; i < lines.length; i++) {
            String temp[] = lines[i].split(":");

            Pair<Integer, Integer> pair = new Pair<>(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));

            products.add(pair);
        }

        Transaction transaction = new Transaction(UUID.fromString(lines[0]), Float.parseFloat(lines[1]), products, vouchers);

        Log.i("Transaction Readed:", transaction.toString());

        return transaction;
    }
}
