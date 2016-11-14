package com.example.joao.cafeteria_client_app.Authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Cafeteria.Cart;
import com.example.joao.cafeteria_client_app.Cafeteria.PastTransactionsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.ProductsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/**
 * Created by josec on 13/11/2016.
 */

public class PastTransactionAuthActivity extends AppCompatActivity implements CallbackPastTransactionAuth {

    Button login_button;
    EditText input_username, input_password;
    ProgressDialog progressDialog;

    PastTransactionAuthActivity pastTransactionAuthActivity;
    SharedPreferences sharedPreferences;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_transactions_auth);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);

        login_button = (Button) findViewById(R.id.btn_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pastTransactionAuthActivity = this;

        if (sharedPreferences.contains("AlreadyLogged")) {
            String json = sharedPreferences.getString("AlreadyLogged", "");

            Gson gson = new Gson();

            startPastTransactionsActivity();

        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = input_username.getText().toString();
                String password = input_password.getText().toString();

                validFields(username, password);

                check(username, password);
            }
        });


    }

    private boolean validFields(String username, String password) {


        if (username.isEmpty() || username.split("\\s+").length > 1) {
            displayInputTextError(input_username, "Insert only one word");
            return false;
        } else {
            removeInputTextError(input_username);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            displayInputTextError(input_password, "Between 4 and 10 alphanumeric characters");
            return false;
        } else {
            removeInputTextError(input_password);
        }

        return true;
    }

    private void check(String username, String password) {
        if (!Cart.getInstance().getCart().isEmpty())
            Cart.getInstance().clearCart();

        if (hasSharedPreferences() && sharedPreferences.getString("alreadyLogged", "").equals("ok")) {

            String storageUsername = sharedPreferences.getString("username", "");
            String storagePassword = sharedPreferences.getString("password", "");


            Log.i("Username Storage: ", "" + storageUsername);
            Log.i("Password Storage: ", storagePassword);
            Log.i("Input username: ", username);
            Log.i("Input password: ", password);

            if (!username.equals(storageUsername) || !password.equals(storagePassword)) {
                Toast.makeText(getBaseContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG).show();


            startPastTransactionsActivity();
        } else {
            Log.i("Entrei no else", "ok");

            RequestParams params = new RequestParams();

            params.put("username", username);
            params.put("password", password);

            progressDialog = new ProgressDialog(PastTransactionAuthActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            try {
                Log.i("Entrei no try", "ok");
                CafeteriaRestClientUsage.pastTransactionAUTH(pastTransactionAuthActivity, params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasSharedPreferences() {
        return sharedPreferences.contains("AlreadyLogged");
    }

    @Override
    public void onPastTransactionsAuthCompleted() {

        progressDialog.dismiss();

        saveSharedPreferences();

        startPastTransactionsActivity();
    }

    @Override
    public void onPastTransactionAuthFailed(String msg) {
        progressDialog.dismiss();

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void startPastTransactionsActivity() {
        Intent intent = new Intent(getApplicationContext(), PastTransactionsActivity.class);
        startActivity(intent);
    }

    private void saveSharedPreferences() {
        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AlreadyLogged", "ok");
        editor.apply();
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
}
