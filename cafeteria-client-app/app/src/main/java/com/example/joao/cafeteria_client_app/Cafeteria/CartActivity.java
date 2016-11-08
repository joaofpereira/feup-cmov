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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.R;


public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CartProductsAdapter cartProductsAdapter;

    TextView total_cart_price, user_name, user_email;

    RecyclerView recyclerView;
    NavigationView navigationView;

    CartActivity cartActivity;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        cartActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.cart_list_id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.cart_layout);
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

        total_cart_price = (TextView) findViewById(R.id.total_cart_price);
        total_cart_price.setText("Total Price: " + String.format("%.2f", Cart.getInstance().getTotalValue()) + " €");

        cartProductsAdapter = new CartProductsAdapter(getBaseContext(), this, Cart.getInstance().getCart());
        RecyclerView.LayoutManager productsManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(productsManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartProductsAdapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            finish();
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_vouchers) {

        } else if (id == R.id.nav_past_transactions) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            cartActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.cart_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
