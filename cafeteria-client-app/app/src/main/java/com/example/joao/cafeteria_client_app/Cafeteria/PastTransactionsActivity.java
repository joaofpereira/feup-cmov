package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.R;
import com.example.joao.cafeteria_client_app.Utils.DividerItemDecoration;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.List;

public class PastTransactionsActivity extends AppCompatActivity implements CallbackPastTransactions, NavigationView.OnNavigationItemSelectedListener {

    TextView user_name, user_email;

    PastTransactionsAdapter pastTransactionsAdapter;

    RecyclerView recyclerView;
    NavigationView navigationView;

    PastTransactionsActivity pastTransactionsActivity;
    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_transactions);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        pastTransactionsActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.past_transactions_list);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.past_transactions_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Start Nav View

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        user_name = (TextView) header.findViewById(R.id.nav_user_name);
        user_email = (TextView) header.findViewById(R.id.nav_user_email);
        user_name.setText(User.getInstance().getName());
        user_email.setText(User.getInstance().getEmail());

        progressDialog = new ProgressDialog(PastTransactionsActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Past Transactions");
        progressDialog.show();

        RequestParams transactions_params = new RequestParams();
        transactions_params.add("userID", User.getInstance().getID().toString());

        try {
            CafeteriaRestClientUsage.getTransactions(pastTransactionsActivity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.past_transactions_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_vouchers) {
            Intent intent = new Intent(getApplicationContext(), VoucherActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_past_transactions) {
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            pastTransactionsActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.past_transactions_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onGetPastTransactionsCompleted(List<Transaction> transactions) {

        pastTransactionsAdapter = new PastTransactionsAdapter(transactions, new PastTransactionsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Transaction transaction) {

                Gson gson = new Gson();
                String transaction_string = gson.toJson(transaction);

                Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
                intent.putExtra("transaction", transaction_string);
                startActivity(intent);
            }
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        RecyclerView.LayoutManager pastTransactionsManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pastTransactionsManager );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pastTransactionsAdapter);

        progressDialog.dismiss();
    }

    @Override
    public void onGetPastTransactionsError(Throwable throwable) {
        progressDialog.dismiss();

        Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }
}
