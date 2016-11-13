package com.example.joao.cafeteriaterminal.Cafeteria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.cafeteriaterminal.QRCODE.ReaderActivity;
import com.example.joao.cafeteriaterminal.R;
import com.google.gson.Gson;

public class TransactionResumeActivity extends AppCompatActivity {

    TransactionTransmitted transaction;
    ProductCompleteAdapter adapter;

    TextView transaction_number, transaction_vouchers, transaction_prev_value, transaction_curr_value;
    RecyclerView recyclerView;
    Button backToScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_resume);

        transaction_number = (TextView) findViewById(R.id.transaction_number);
        transaction_vouchers = (TextView) findViewById(R.id.transaction_vouchers);
        transaction_prev_value = (TextView) findViewById(R.id.transaction_prev_value);
        transaction_curr_value = (TextView) findViewById(R.id.transaction_curr_value);

        recyclerView = (RecyclerView) findViewById(R.id.products_complete_id);

        backToScan = (Button) findViewById(R.id.back_to_scan);

        backToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ReaderActivity.class);
                startActivity(intent);
            }
        });

        Gson gson = new Gson();

        String trans_S = getIntent().getStringExtra("transaction");
        transaction = gson.fromJson(trans_S, TransactionTransmitted.class);

        transaction_number.setText(Html.fromHtml("<font color='#3F51B5'>Order Number: </font>" + transaction.getId()), TextView.BufferType.SPANNABLE);
        transaction_vouchers.setText(Html.fromHtml("<font color='#3F51B5'>Vouchers: </font>" + transaction.getVouchersUsed()), TextView.BufferType.SPANNABLE);
        transaction_prev_value.setText(Html.fromHtml("<font color='#3F51B5'>Previously Value: </font>" + String.format(java.util.Locale.US,"%.2f", transaction.getPreviouslyTotalPrice()) + " €"), TextView.BufferType.SPANNABLE);
        transaction_curr_value.setText(Html.fromHtml("<font color='#3F51B5'>Final Value: </font>" + String.format(java.util.Locale.US,"%.2f", transaction.getTransactionTotalPrice()) + " €"), TextView.BufferType.SPANNABLE);

        adapter = new ProductCompleteAdapter(transaction.getProducts());
        RecyclerView.LayoutManager productsManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(productsManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
