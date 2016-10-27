package com.example.joao.cafeteria_client_app.Cafeteria;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.R;

import org.json.JSONException;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Product> prod_list;
    Button add_to_cart_button;
    TextView _price;
    EditText quantity;
    ProductActivity productActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        quantity = (EditText) findViewById(R.id.input_quantity);
        add_to_cart_button = (Button) findViewById(R.id.btn_addToCart);
        _price = (TextView) findViewById(R.id.price);

        productActivity= this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*try {
            CafeteriaRestClientUsage.getAllProduct(productActivity);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
        add_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*
              * TO-DO
              *
              *
              *
            }
        });*/

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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.products_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ArrayList<Product> getProd_list() {
        return prod_list;
    }

    public void setProd_list(ArrayList<Product> prod_list) {
        this.prod_list = prod_list;
    }

    public void add_To_Prod_list(Product prod) {
        this.prod_list.add(prod);
    }
}
