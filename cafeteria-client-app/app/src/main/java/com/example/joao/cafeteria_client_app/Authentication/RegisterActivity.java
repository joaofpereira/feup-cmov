package com.example.joao.cafeteria_client_app.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.R;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;


public class RegisterActivity extends AppCompatActivity implements CallbackRegister {

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.register_layout);

        registerActivity = this;

        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validFields()) {
                    register_btn.setEnabled(true);

                    return;
                }

                RequestParams params = new RequestParams();

                params.put("name", input_name.getText().toString());
                params.put("username", input_username.getText().toString());
                params.put("email", input_email.getText().toString());
                params.put("password", input_password.getText().toString());
                params.put("cardNumber", creditCardForm.getCreditCard().getCardNumber().replaceAll("\\s+", ""));
                params.put("securityCode", creditCardForm.getCreditCard().getSecurityCode());
                params.put("expMonth", creditCardForm.getCreditCard().getExpMonth().toString());
                params.put("expYear", creditCardForm.getCreditCard().getExpYear().toString());

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

    @Override
    public void onRegisterFailed(int code, String msg) {
        if (code == 400) {
            displayInputTextError(input_username, msg);
        } else if (code == 401) {
            displayInputTextError(input_email, msg);
        }

        register_btn.setEnabled(true);
    }

    private void saveSharedPreferences() {
        sharedPreferences = getSharedPreferences("com.example.joao.cafeteria_client_app", Activity.MODE_PRIVATE);

        Gson gson = new Gson();
        String user = gson.toJson(this.user);

        Log.i("USER JSON: ", user);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user);
        editor.putString("pin", this.pin);
        editor.apply();
    }

    private boolean validFields() {
        String name = input_name.getText().toString();
        String username = input_username.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            displayInputTextError(input_name, "At least 3 characters");
            return false;
        } else {
            removeInputTextError(input_name);
        }

        if (username.isEmpty() || username.split("\\s+").length > 1) {
            displayInputTextError(input_username, "Insert only one word");
            return false;
        } else {
            removeInputTextError(input_username);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            displayInputTextError(input_email, "Enter a valid email address");
            return false;
        } else {
            removeInputTextError(input_email);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            displayInputTextError(input_password, "Between 4 and 10 alphanumeric characters");
            return false;
        } else {
            removeInputTextError(input_password);
        }

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
}
