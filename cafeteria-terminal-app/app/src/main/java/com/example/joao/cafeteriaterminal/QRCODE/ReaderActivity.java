package com.example.joao.cafeteriaterminal.QRCODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.joao.cafeteriaterminal.API.CafeteriaRestTerminalUsage;
import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.example.joao.cafeteriaterminal.Cafeteria.TransactionsList;
import com.example.joao.cafeteriaterminal.Cafeteria.Voucher;
import com.example.joao.cafeteriaterminal.Cafeteria.VouchersList;
import com.example.joao.cafeteriaterminal.R;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class ReaderActivity extends AppCompatActivity implements CallbackTransaction {

    private Button scan_btn;

    ReaderActivity readerActivity;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        readerActivity = this;

        scan_btn = (Button) findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(readerActivity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_terminal_app", Activity.MODE_PRIVATE);

        try {
            loadVouchers();
            loadLocallyTransactions();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Vouchers: ", VouchersList.getInstance().toString());
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

                    JSONArray products = new JSONArray();

                    for (int i = 0; i < transaction.getProducts().size(); i++) {
                        JSONObject obj = new JSONObject();
                        obj.put("product-id", transaction.getProducts().get(i).first);
                        obj.put("product-amount", transaction.getProducts().get(i).second);

                        products.put(obj);
                    }

                    params.put("products", products);

                    if (isOnline())
                       // try {
                            verifyTransactionVouchers(transaction);
                                //CafeteriaRestTerminalUsage.confirmTransaction(readerActivity, params);
                        /*} catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    else {
                        TransactionsList.getInstance().add(transaction);

                        Gson gson = new Gson();
                        String transactions_list = gson.toJson(TransactionsList.getInstance().getTransactions());

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("transactions", transactions_list);
                        editor.apply();
                    }

                    Log.i("Transactions: ", TransactionsList.getInstance().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean verifyTransactionVouchers(Transaction transaction) throws NoSuchProviderException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA1withRSA");

        signature.initVerify(public_key);
        signature.update(pair.getKey().toString().getBytes());

        boolean verify_result = signature.verify(sig); //sign é a signature do voucher em bytes

        return false;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadVouchers() {
        if (sharedPreferences.contains("vouchers")) {
            String json = sharedPreferences.getString("vouchers", "");

            Type listType = new TypeToken<ArrayList<Voucher>>() {
            }.getType();
            VouchersList.getInstance().setVouchers((List<Voucher>) new Gson().fromJson(json, listType));
        }
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
    public void onTransactionRegisterComplete() {
        // save vouchers
        Gson gson = new Gson();
        String vouchers_list = gson.toJson(VouchersList.getInstance().getVouchers());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("vouchers", vouchers_list);
        editor.apply();
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
}
