package com.example.joao.cafeteria_client_app.Authentication;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Cafeteria.Callback;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;


public class RegisterActivity extends AppCompatActivity implements Callback, NavigationView.OnNavigationItemSelectedListener {

    EditText input_name = null, input_username = null, input_email = null, input_password = null;
    CreditCardForm creditCardForm;
    TextView _loginLink;
    Button register_btn = null;

    RegisterActivity registerActivity;
    SharedPreferences sharedPreferences;

    String pin;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        Log.i("", "\nUUID: " + sharedPreferences.getString("uuid", "") + "\nHash_pin: " + sharedPreferences.getString("hash_pin", "") + "\n");

        input_name = (EditText) findViewById(R.id.register_input_name);
        input_username = (EditText) findViewById(R.id.register_input_username);
        input_email = (EditText) findViewById(R.id.register_input_email);
        input_password = (EditText) findViewById(R.id.register_input_password);
        creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);

        register_btn = (Button) findViewById(R.id.btn_signup);

        _loginLink = (TextView) findViewById(R.id.link_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.register_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registerActivity = this;

        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestParams params = new RequestParams();

                params.put("name", input_name.getText().toString());
                params.put("username", input_username.getText().toString());
                params.put("email", input_email.getText().toString());
                params.put("password", input_password.getText().toString());
                params.put("cardNumber", creditCardForm.getCreditCard().getCardNumber());
                params.put("securityCode", creditCardForm.getCreditCard().getSecurityCode());
                params.put("expMonth", creditCardForm.getCreditCard().getExpMonth().toString());
                params.put("expUser", creditCardForm.getCreditCard().getExpYear().toString());

                Log.i("POST", params.toString());

                try {
                    CafeteriaRestClientUsage.register(registerActivity, params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                registerActivity.finish();
            }
        });
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.register_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRegisterCompleted(String pin, User user) {
        this.pin = pin;
        this.user = user;

        Log.i("", this.user.toString() + "\nPin: " + this.pin + "\n");

        saveSharedPreferences();

        Intent intent = new Intent(registerActivity, ShowPinActivity.class);
        intent.putExtra("pin", this.pin);

        startActivity(intent);
    }

    private void saveSharedPreferences() {
        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uuid", user.getID().toString());
        editor.putString("hash_pin", user.getHash_pin());
        editor.apply();
    }
}
