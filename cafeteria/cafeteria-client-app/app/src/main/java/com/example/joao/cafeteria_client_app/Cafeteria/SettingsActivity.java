package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Authentication.CallbackRegister;
import com.example.joao.cafeteria_client_app.Authentication.LoginActivity;
import com.example.joao.cafeteria_client_app.Authentication.PastTransactionAuthActivity;
import com.example.joao.cafeteria_client_app.Authentication.RegisterActivity;
import com.example.joao.cafeteria_client_app.Authentication.ShowPinActivity;
import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/**
 * Created by josec on 13/11/2016.
 */

public class SettingsActivity extends AppCompatActivity implements CallbackSettings, NavigationView.OnNavigationItemSelectedListener {

    CreditCardForm creditCardForm;
    Button card_btn = null;
    ProgressDialog progressDialog;

    NavigationView navigationView;
    SettingsActivity settingsActivity;
    SharedPreferences sharedPreferences;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        userID = sharedPreferences.getString("user", "");
        creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);

        card_btn = (Button) findViewById(R.id.btn_update_card);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsActivity = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.settings_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);


        card_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validFields()) {
                    card_btn.setEnabled(true);
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("userID",User.getInstance().getID());
                params.put("cardNumber", creditCardForm.getCreditCard().getCardNumber().replaceAll("\\s+", ""));
                params.put("securityCode", creditCardForm.getCreditCard().getSecurityCode());
                params.put("expMonth", creditCardForm.getCreditCard().getExpMonth().toString());
                params.put("expYear", creditCardForm.getCreditCard().getExpYear().toString());

                progressDialog = new ProgressDialog(SettingsActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Creditcard...");
                progressDialog.show();

                try {
                    CafeteriaRestClientUsage.updateCard(settingsActivity, params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onCreditCardChangeCompleted(int creditCardID) {
        progressDialog.dismiss();
        User.getInstance().createCreditCard(creditCardID,creditCardForm.getCreditCard().getCardNumber().replaceAll("\\s+", ""),creditCardForm.getCreditCard().getExpMonth().toString(),creditCardForm.getCreditCard().getExpYear().toString());

        Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
        startActivity(intent);

    }


    @Override
    public void onCreditCardChangeFailed(int code, String msg) {
        progressDialog.dismiss();
       card_btn.setEnabled(true);
    }



    private boolean validFields() {

        if (!creditCardForm.isCreditCardValid()) {
            Toast.makeText(getBaseContext(), "Invalid Credit Card", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void displayInputTextError(EditText ed, String msg) {
        ed.requestFocus();
        ed.setTextColor(Color.parseColor("#FF0000"));
        ed.setError(msg);
    }

    private void removeInputTextError(EditText ed) {
        ed.setError(null);
        ed.setTextColor(Color.parseColor("#000000"));
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
            Intent intent = new Intent(getApplicationContext(), PastTransactionAuthActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            settingsActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.settings_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}