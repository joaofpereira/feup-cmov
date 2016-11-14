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
import com.example.joao.cafeteriaterminal.Cafeteria.BlackList;
import com.example.joao.cafeteriaterminal.Cafeteria.BlackListUser;
import com.example.joao.cafeteriaterminal.Cafeteria.BlacklistActivity;
import com.example.joao.cafeteriaterminal.Cafeteria.Callback;
import com.example.joao.cafeteriaterminal.Cafeteria.ErrorActivity;
import com.example.joao.cafeteriaterminal.Cafeteria.Product;
import com.example.joao.cafeteriaterminal.Cafeteria.ProductComplete;
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

            if (sharedPreferences.contains("blacklist")) {
                String json = sharedPreferences.getString("blacklist", "");

                Type listType = new TypeToken<ArrayList<BlackListUser>>() {
                }.getType();
                BlackList.getInstance().setBlackList((List<BlackListUser>) new Gson().fromJson(json, listType));
            } else {
                CafeteriaRestTerminalUsage.getBlacklist(this);
            }

            loadLocallyTransactions();
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

                    if (!BlackList.getInstance().userExistsInBlacklist(transaction.getUserID()))
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

                                // start presentation to the user

                                int id = 0;

                                List<ProductComplete> products_trans = new ArrayList<>();

                                for (int i = 0; i < transaction.getProducts().size(); i++) {
                                    Product p = ProductsList.getInstance().getProductByID(transaction.getProducts().get(i).first);
                                    int amount = transaction.getProducts().get(i).second;

                                    products_trans.add(new ProductComplete(p.getName(), amount, p.getPrice() * amount));
                                }

                                boolean coffee = false, popcorn = false, discount = false;

                                for (int i = 0; i < transaction.getVouchers().size(); i++) {
                                    if (transaction.getVouchers().get(i).getType() == 1)
                                        popcorn = true;
                                    else if (transaction.getVouchers().get(i).getType() == 2)
                                        coffee = true;
                                    else
                                        discount = true;
                                }

                                float transactionTotalPrice;

                                if (discount)
                                    transactionTotalPrice = (float) ((double) transaction.getTotalValue() * 0.95);
                                else
                                    transactionTotalPrice = transaction.getTotalValue();

                                onTransactionRegisterComplete(new TransactionTransmitted(popcorn, coffee, discount, products_trans, transactionTotalPrice, id));

                            } else {
                                BlackListUser blu = new BlackListUser(0, transaction.getUserID(), "Invalid Vouchers");
                                BlackList.getInstance().add(blu);
                                saveBlacklist(BlackList.getInstance().getBlacklist());
                            }
                        }
                    else
                        startBlacklistActivity(BlackList.getInstance().getByID(transaction.getUserID()));

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

            if (!verify_result)
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
        } else {
            startScan();
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
            Log.i("Entrei no isOn", "ok");
            RequestParams params = new RequestParams();

            Transaction t = TransactionsList.getInstance().get(0);

            try {
                params.put("userID", t.getUserID());
                params.put("totalValue", t.getTotalValue());

                JSONArray vouchers = new JSONArray();

                for (int i = 0; i < t.getVouchers().size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", t.getVouchers().get(i).getID());
                    obj.put("type", t.getVouchers().get(i).getType());
                    obj.put("serial", t.getVouchers().get(i).getSerial());
                    obj.put("signature", t.getVouchers().get(i).getSignature());

                    vouchers.put(obj);
                }

                params.put("vouchers", vouchers);

                JSONArray products = new JSONArray();

                for (int j = 0; j < t.getProducts().size(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("product-id", t.getProducts().get(j).first.toString());
                    obj.put("product-amount", t.getProducts().get(j).second.toString());

                    products.put(obj);
                }

                params.put("products", products);

                try {
                    Log.i("Entrei no try", "ok");
                    CafeteriaRestTerminalUsage.postTransactions(readerActivity, params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            startScan();
        }
    }

    @Override
    public void onUpdateTransactionsComplete() {
        TransactionsList.getInstance().getTransactions().remove(0);

        if (TransactionsList.getInstance().getSize() > 0)
            updateTransactionsOnServer();
        else {
            startScan();

            TransactionsList.getInstance().getTransactions().clear();
            sharedPreferences.edit().remove("transactions").commit();

            if (sharedPreferences.contains("transactions"))
                Log.i("SHAREDPREFERENCES: ", "TEM TRANSACTIONS");
            else
                Log.i("SHAREDPREFERENCES: ", "NAO TEM TRANSACTIONS");
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
    public void onGetProductsCompleted(List<Product> products) {
        ProductsList.getInstance().setProducts(products);

        saveProductsList(products);

        try {
            CafeteriaRestTerminalUsage.getBlacklist(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetBlacklistCompleted(List<BlackListUser> blacklist) {
        BlackList.getInstance().setBlackList(blacklist);

        saveBlacklist(blacklist);

        Log.i("Print Black List: ", blacklist.toString());

        startScan();
    }

    @Override
    public void onBlackListInserted(BlackListUser blu) {
        BlackList.getInstance().add(blu);
        saveBlacklist(BlackList.getInstance().getBlacklist());

        startBlacklistActivity(blu);
    }

    public void startBlacklistActivity(BlackListUser blu) {
        Gson gson = new Gson();
        String blacklist_user = gson.toJson(blu);

        Intent intent = new Intent(getBaseContext(), BlacklistActivity.class);

        intent.putExtra("blacklistUser", blacklist_user);
        startActivity(intent);
    }

    @Override
    public void onBlackListInsertedInUpdateTransactions(BlackListUser blu) {
        BlackList.getInstance().add(blu);
        saveBlacklist(BlackList.getInstance().getBlacklist());

        onUpdateTransactionsComplete();
    }

    @Override
    public void onUserNotExists(String message) {
        startErrorActivity(message);
    }

    @Override
    public void onUserNotExistsInUpdateTransactions() {
        onUpdateTransactionsComplete();
    }

    private void startErrorActivity(String message) {
        Intent intent = new Intent(getBaseContext(), ErrorActivity.class);

        intent.putExtra("message", message);
        startActivity(intent);
    }

    private void saveProductsList(List<Product> products) {
        Gson gson = new Gson();
        String products_list = gson.toJson(products);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("products", products_list);
        editor.apply();
    }

    private void saveBlacklist(List<BlackListUser> blacklist) {
        Gson gson = new Gson();
        String blacklistS = gson.toJson(blacklist);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("blacklist", blacklistS);
        editor.apply();
    }
}
