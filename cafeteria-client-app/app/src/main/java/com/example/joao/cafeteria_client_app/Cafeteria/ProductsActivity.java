package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements CallbackProducts, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    ProductsAdapter productsAdapter;

    TextView user_name, user_email;

    RecyclerView recyclerView;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
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

        progressDialog = new ProgressDialog(ProductsActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Products");
        progressDialog.show();

        if(hasSharedPreferences()) {
            String json = sharedPreferences.getString("products", "");

            Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> productsList = new Gson().fromJson(json, listType);

            onGetProductsCompleted(productsList);
        } else {
            try {
                CafeteriaRestClientUsage.getProducts(productsActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                moveTaskToBack(true);
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
            Intent intent = new Intent(productsActivity, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_vouchers) {

        } else if (id == R.id.nav_past_transactions) {
            Intent intent = new Intent(getApplicationContext(), PastTransactionsActivity.class);
            startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.products_bar_cart) {
            Intent intent = new Intent(productsActivity, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.products_bar_refresh) {
            // empty cart
            Cart.getInstance().clearCart();

            // start updating process
            progressDialog.setMessage("Updating Products");
            progressDialog.show();
            try {
                CafeteriaRestClientUsage.getProducts(productsActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetProductsCompleted(List<Product> products) {
        ProductsList.getInstance().setProducts(products);

        saveProductsList(products);

        productsAdapter = new ProductsAdapter(ProductsList.getInstance().getProducts());
        RecyclerView.LayoutManager productsManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(productsManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        progressDialog.dismiss();
    }

    @Override
    public void onGetProductsError(Throwable throwable) {
        // TODO handling exceptions later

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        progressDialog.dismiss();

        Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    private void saveProductsList(List<Product> products) {
        Gson gson = new Gson();
        String products_list = gson.toJson(products);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("products", products_list);
        editor.apply();
    }

    private boolean hasSharedPreferences() {
        return sharedPreferences.contains("products");
    }

    @Override
    public void onRefresh() {
        // empty cart
        Cart.getInstance().clearCart();

        try {
            CafeteriaRestClientUsage.getProducts(productsActivity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
