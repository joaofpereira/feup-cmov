package com.example.joao.cafeteria_client_app.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.Cafeteria.ProductsActivity;
import com.example.joao.cafeteria_client_app.R;

public class ShowPinActivity extends AppCompatActivity {

    TextView show_pin;
    Button ok_btn;

    ShowPinActivity showPinActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pin);

        show_pin = (TextView) findViewById(R.id.show_pin_id);
        ok_btn = (Button) findViewById(R.id.show_pin_btn_id);

        showPinActivity = this;

        Intent intent= getIntent();

        show_pin.setText(intent.getExtras().getString("pin"));
        Log.i("", "Pin Sent Is: " + intent.getExtras().getString("pin") + "\n");

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(showPinActivity, ProductsActivity.class);
                startActivity(intent);
            }
        });
    }
}
