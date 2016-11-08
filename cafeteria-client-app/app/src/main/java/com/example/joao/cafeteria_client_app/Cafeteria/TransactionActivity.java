package com.example.joao.cafeteria_client_app.Cafeteria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class TransactionActivity extends AppCompatActivity {

    TextView transaction_value, transaction_total_products, transaction_type_products, transaction_date;
    RecyclerView recyclerView;

    Transaction transaction;

    TransactionAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        transaction_date = (TextView) findViewById(R.id.transaction_date);
        transaction_type_products = (TextView) findViewById(R.id.transaction_type_products);
        transaction_total_products = (TextView) findViewById(R.id.transaction_total_products);
        transaction_value = (TextView) findViewById(R.id.transaction_value);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("transaction");

            Gson gson = new Gson();
            Transaction transaction = gson.fromJson(value, Transaction.class);

            setTitle("Transaction nº. " + transaction.getId());
            transaction_date.setText(Html.fromHtml("<font color='#3F51B5'>Date: </font>" + new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy', at 'hh:mm:ss").format(transaction.getDate())), TextView.BufferType.SPANNABLE);
            transaction_type_products.setText(Html.fromHtml("<font color='#3F51B5'>Type of Products: </font>" + transaction.getProducts().size()), TextView.BufferType.SPANNABLE);
            transaction_total_products.setText(Html.fromHtml("<font color='#3F51B5'>Total Products: </font>" + transaction.getTotalProducts()), TextView.BufferType.SPANNABLE);
            transaction_value.setText(Html.fromHtml("<font color='#3F51B5'>Total Value: </font>" + transaction.getTotalPrice() + " €"), TextView.BufferType.SPANNABLE);

            this.transaction = transaction;

            recyclerView = (RecyclerView) findViewById(R.id.transaction_products_list);

            productsAdapter = new TransactionAdapter(transaction.getProducts());
            RecyclerView.LayoutManager productsManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(productsManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(productsAdapter);
        }
    }
}
