package com.example.joao.cafeteria_client_app.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

public class ShowPinActivity extends AppCompatActivity {

    TextView show_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pin);

        show_pin = (TextView) findViewById(R.id.show_pin_id);

        Intent intent= getIntent();

        show_pin.setText(intent.getExtras().getString("pin"));
        Log.i("", "Pin Sent Is: " + intent.getExtras().getString("pin") + "\n");
    }
}
