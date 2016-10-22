package com.example.joao.cafeteria_client_app.Authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.joao.cafeteria_client_app.R;


public class RegisterActivity extends AppCompatActivity {
    EditText name_field = null, username_field = null, email_field = null, password_field = null, creditCardInfo_field = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_field = (EditText) findViewById(R.id.register_name_field);
        username_field = (EditText) findViewById(R.id.register_username_field);
        email_field = (EditText) findViewById(R.id.register_email_field);
        password_field = (EditText) findViewById(R.id.register_password_field);
        creditCardInfo_field = (EditText) findViewById(R.id.register_credit_card_field);

    }
}
