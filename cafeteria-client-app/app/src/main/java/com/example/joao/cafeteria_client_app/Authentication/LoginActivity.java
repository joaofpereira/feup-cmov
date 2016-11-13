package com.example.joao.cafeteria_client_app.Authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Cafeteria.Cart;
import com.example.joao.cafeteria_client_app.Cafeteria.ProductsActivity;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity implements CallbackLogin {

    Button login_button;
    TextView _signupLink;
    EditText input_email, input_pin;
    ProgressDialog progressDialog;

    LoginActivity loginActivity;
    SharedPreferences sharedPreferences;

    String pin;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        input_email = (EditText) findViewById(R.id.input_email);
        input_pin = (EditText) findViewById(R.id.input_pin);

        login_button = (Button) findViewById(R.id.btn_login);

        _signupLink = (TextView) findViewById(R.id.link_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginActivity = this;

        if (sharedPreferences.contains("user")) {
            String json = sharedPreferences.getString("user", "");

            Gson gson = new Gson();
            User.createInstance(gson.fromJson(json, User.class));

            startProductsActivity();

        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String pin = input_pin.getText().toString();

                validFields(email, pin);

                login(email, pin);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                loginActivity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.login_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validFields(String email, String pin) {

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            displayInputTextError(input_email, "Enter a valid email address");
            return false;
        } else {
            removeInputTextError(input_email);
        }

        if (pin.isEmpty() || pin.length() != 4 || !pin.matches("[0-9]+")) {
            displayInputTextError(input_pin, "Pin has only 4 digits");
            return false;
        } else {
            removeInputTextError(input_pin);
        }

        return true;
    }

    private void login(String email, String pin) {
        if(!Cart.getInstance().getCart().isEmpty())
            Cart.getInstance().clearCart();

        if (hasSharedPreferences()) {
            String json = sharedPreferences.getString("user", "");

            int storagePin = Integer.parseInt(sharedPreferences.getString("pin", ""));

            Gson gson = new Gson();
            User storageUser = gson.fromJson(json, User.class);

            Log.i("Pin Storage: ", "" + storagePin);
            Log.i("Email User Storage: ", storageUser.getEmail());
            Log.i("Input Pin: ", pin);
            Log.i("Input Email: ", email);

            if (Integer.parseInt(pin) != storagePin || !storageUser.getEmail().equals(email)) {
                Toast.makeText(getBaseContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG).show();

            User.createInstance(storageUser);

            startProductsActivity();
        } else {
            Log.i("Entrei no else", "ok");

            RequestParams params = new RequestParams();

            params.put("email", email);
            params.put("pin", pin);

            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            try {
                Log.i("Entrei no try", "ok");
                CafeteriaRestClientUsage.login(loginActivity, params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasSharedPreferences() {
        return sharedPreferences.contains("user") && sharedPreferences.contains("pin");
    }

    @Override
    public void onLoginCompleted(String pin, User user) {
        this.user = user;
        this.pin = pin;

        progressDialog.dismiss();

        saveSharedPreferences();

        User.createInstance(user);

        startProductsActivity();
    }

    @Override
    public void onLoginFailed(String msg) {
        progressDialog.dismiss();

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void startProductsActivity() {
        Intent intent = new Intent(loginActivity, ProductsActivity.class);
        startActivity(intent);
    }

    private void saveSharedPreferences() {
        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        Gson gson = new Gson();
        String user = gson.toJson(this.user);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user);
        editor.putString("pin", this.pin);
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
