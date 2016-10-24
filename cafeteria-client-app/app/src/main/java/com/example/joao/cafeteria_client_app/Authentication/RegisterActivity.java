package com.example.joao.cafeteria_client_app.Authentication;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joao.cafeteria_client_app.API.CafeteriaRestClientUsage;
import com.example.joao.cafeteria_client_app.Cafeteria.Callback;
import com.example.joao.cafeteria_client_app.Cafeteria.User;
import com.example.joao.cafeteria_client_app.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;


public class RegisterActivity extends AppCompatActivity implements Callback{

    EditText name_field = null, username_field = null, email_field = null, password_field = null, creditCardInfo_field = null;
    Button register_apply = null;

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
        /*if(sharedPreferences.contains("uuid") && sharedPreferences.contains("hash_pin")){
            Log.i("", "\nUUID: " + sharedPreferences.getString("uuid", "") + "\nHash_pin: " + sharedPreferences.getString("hash_pin", "") + "\n");
        }
        else {*/

            name_field = (EditText) findViewById(R.id.register_name_field);
            username_field = (EditText) findViewById(R.id.register_username_field);
            email_field = (EditText) findViewById(R.id.register_email_field);
            password_field = (EditText) findViewById(R.id.register_password_field);
            creditCardInfo_field = (EditText) findViewById(R.id.register_credit_card_field);

            register_apply = (Button) findViewById(R.id.register_apply_button);

            registerActivity = this;

            register_apply.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RequestParams params = new RequestParams();

                    params.put("name", name_field.getText().toString());
                    params.put("username", username_field.getText().toString());
                    params.put("email", email_field.getText().toString());
                    params.put("password", password_field.getText().toString());
                    params.put("creditCardInfo", creditCardInfo_field.getText());

                    try {
                        CafeteriaRestClientUsage.register(registerActivity, params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        //}
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
