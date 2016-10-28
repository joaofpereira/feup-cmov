package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements CallbackProducts, NavigationView.OnNavigationItemSelectedListener {

    List<Product> productsList = new ArrayList<Product>();
    ProductsAdapter productsAdapter;

    RecyclerView recyclerView;

    ProductsActivity productsActivity;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        productsActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.products_list_id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            CafeteriaRestClientUsage.getProducts(productsActivity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
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
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_vouchers) {

        } else if (id == R.id.nav_past_transactions) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            productsActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onGetProductsCompleted(List<Product> products) {
        Log.i("", "Entrei no onGet\n");

        this.productsList = products;

        productsAdapter = new ProductsAdapter(productsList);
        RecyclerView.LayoutManager productsManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(productsManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);
    }
}
