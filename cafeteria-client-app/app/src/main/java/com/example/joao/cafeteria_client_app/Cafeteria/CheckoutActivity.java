package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.R;

public class CheckoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    CheckoutActivity checkoutActivity;

    NavigationView navigationView;

    EditText checkout_pin;
    TextView user_name, user_email;
    Button cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        checkoutActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.checkout_layout);
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

        checkout_pin = (EditText) findViewById(R.id.checkout_pin);
        checkout_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.i("START: ", "" + start);
                Log.i("BEFORE: ", "" + before);
                Log.i("COUNT: ", "" + count);
                Log.i("CHARSEQUENCE: ", charSequence.toString());

                Log.i("PIN: ", sharedPreferences.getString("pin", ""));

                if (start == 3 && charSequence.length() == 4)
                    if (charSequence.toString().equals(sharedPreferences.getString("pin", ""))) {

                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);

                        Toast.makeText(getBaseContext(), "Wrong pin", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("EDITABLE: ", editable.toString());
            }
        });

        cancel_button = (Button) findViewById(R.id.checkout_cancel_btn);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.getInstance().clearCart();
                Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {
        } else if (id == R.id.nav_cart) {
        } else if (id == R.id.nav_vouchers) {

        } else if (id == R.id.nav_past_transactions) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            checkoutActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.checkout_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
