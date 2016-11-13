package com.example.joao.cafeteriaterminal.QRCODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.joao.cafeteriaterminal.API.CafeteriaRestTerminalUsage;
import com.example.joao.cafeteriaterminal.API.PublicKeyReader;
import com.example.joao.cafeteriaterminal.Cafeteria.Callback;
import com.example.joao.cafeteriaterminal.Cafeteria.Product;
import com.example.joao.cafeteriaterminal.Cafeteria.ProductsList;
import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionResumeActivity;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionTransmitted;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionsList;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class ReaderActivity extends AppCompatActivity implements Callback {

    PublicKey publicKey;

    ReaderActivity readerActivity;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readerActivity = this;

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_terminal_app", Activity.MODE_PRIVATE);

        try {
            loadLocallyTransactions();

            if (sharedPreferences.contains("publicKey")) {
                publicKey = PublicKeyReader.getKey(sharedPreferences.getString("publicKey", ""));
            } else
                CafeteriaRestTerminalUsage.getPublicKey(this);

            if (sharedPreferences.contains("products")) {
                String json = sharedPreferences.getString("products", "");

                Type listType = new TypeToken<ArrayList<Product>>() {
                }.getType();
                ProductsList.getInstance().setProducts((List<Product>) new Gson().fromJson(json, listType));
            } else {
                CafeteriaRestTerminalUsage.getProducts(this);
            }

            startScan();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                try {
                    Transaction transaction = CafeteriaRestTerminalUsage.createTransaction(result.getContents());

                    RequestParams params = new RequestParams();

                    params.put("userID", transaction.getUserID());
                    params.put("totalValue", transaction.getTotalValue());

                    JSONArray vouchers = new JSONArray();

                    for (int i = 0; i < transaction.getVouchers().size(); i++) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", transaction.getVouchers().get(i).getID());
                        obj.put("type", transaction.getVouchers().get(i).getType());
                        obj.put("serial", transaction.getVouchers().get(i).getSerial());
                        obj.put("signature", transaction.getVouchers().get(i).getSignature());

                        vouchers.put(obj);
                    }

                    params.put("vouchers", vouchers);

                    JSONArray products = new JSONArray();

                    for (int i = 0; i < transaction.getProducts().size(); i++) {
                        JSONObject obj = new JSONObject();
                        obj.put("product-id", transaction.getProducts().get(i).first);
                        obj.put("product-amount", transaction.getProducts().get(i).second);

                        products.put(obj);
                    }

                    params.put("products", products);

                    if (isOnline())
                        try {
                            CafeteriaRestTerminalUsage.confirmTransaction(readerActivity, params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    else {

                        if (verifyTransactionVouchers(transaction)) {
                            TransactionsList.getInstance().add(transaction);

                            Gson gson = new Gson();
                            String transactions_list = gson.toJson(TransactionsList.getInstance().getTransactions());

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("transactions", transactions_list);
                            editor.apply();
                        } else {
                            // TODO users black list
                        }
                    }

                    Log.i("Transactions: ", TransactionsList.getInstance().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean verifyTransactionVouchers(Transaction transaction) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withRSA");

        for (int i = 0; i < transaction.getVouchers().size(); i++) {
            signature.initVerify(publicKey);

            String serialS = String.format("%04d", transaction.getVouchers().get(i).getSerial());

            signature.update(serialS.getBytes());

            boolean verify_result = signature.verify(Base64.decode(transaction.getVouchers().get(i).getSignature(), Base64.DEFAULT));

            if(!verify_result)
                return false;
        }

        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadLocallyTransactions() throws JSONException {
        if (sharedPreferences.contains("transactions")) {
            String json = sharedPreferences.getString("transactions", "");

            Type listType = new TypeToken<ArrayList<Transaction>>() {
            }.getType();
            TransactionsList.getInstance().setTransactions((List<Transaction>) new Gson().fromJson(json, listType));

            Log.i("Transaction AFTER: ", TransactionsList.getInstance().toString());

            updateTransactionsOnServer();
        }
    }

    private void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(readerActivity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void updateTransactionsOnServer() {
        if (isOnline()) {
            JSONArray transactions = new JSONArray();

            for (int i = 0; i < TransactionsList.getInstance().getSize(); i++) {
                Transaction t = TransactionsList.getInstance().get(i);

                JSONObject transaction = new JSONObject();

                try {
                    transaction.put("userID", t.getUserID());
                    transaction.put("totalValue", t.getTotalValue());

                    JSONArray products = new JSONArray();

                    for (int j = 0; j < t.getProducts().size(); j++) {
                        JSONObject obj = new JSONObject();
                        obj.put("product-id", t.getProducts().get(j).first.toString());
                        obj.put("product-amount", t.getProducts().get(j).second.toString());

                        products.put(obj);
                    }

                    transaction.put("products", products);

                    transactions.put(i, transaction);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RequestParams parameters = new RequestParams();

            parameters.put("transactions", transactions);

            try {
                CafeteriaRestTerminalUsage.postTransactions(readerActivity, parameters);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetPublicKeyCompleted(String publicKey) {
        this.publicKey = PublicKeyReader.getKey(publicKey);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("publicKey", publicKey);
        editor.apply();

        try {
            CafeteriaRestTerminalUsage.getProducts(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransactionRegisterComplete(TransactionTransmitted t) {
        Gson gson = new Gson();
        String transaction = gson.toJson(t);

        Intent intent = new Intent(getBaseContext(), TransactionResumeActivity.class);
        intent.putExtra("transaction", transaction);
        startActivity(intent);
    }

    @Override
    public void onUpdateTransactionsComplete() {
        TransactionsList.getInstance().getTransactions().clear();
        sharedPreferences.edit().remove("transactions").commit();

        if (sharedPreferences.contains("transaction"))
            Log.i("SHAREDPREFERENCES: ", "TEM TRANSACTIONS");
        else
            Log.i("SHAREDPREFERENCES: ", "NAO TEM TRANSACTIONS");
    }

    @Override
    public void onGetProductsCompleted(List<Product> products) {
        ProductsList.getInstance().setProducts(products);

        saveProductsList(products);

        startScan();
    }

    private void saveProductsList(List<Product> products) {
        Gson gson = new Gson();
        String products_list = gson.toJson(products);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("products", products_list);
        editor.apply();
    }
}
