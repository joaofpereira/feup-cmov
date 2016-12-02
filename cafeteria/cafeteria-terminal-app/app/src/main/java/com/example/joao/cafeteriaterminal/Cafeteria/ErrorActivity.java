package com.example.joao.cafeteriaterminal.Cafeteria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.cafeteriaterminal.QRCODE.ReaderActivity;
import com.example.joao.cafeteriaterminal.R;

public class ErrorActivity extends AppCompatActivity {

    TextView message;
    Button backToScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        String messageS = getIntent().getStringExtra("message");

        message = (TextView) findViewById(R.id.error_message);
        backToScan = (Button) findViewById(R.id.back_to_scan);

        message.setText(messageS);

        backToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ReaderActivity.class);
                startActivity(intent);
            }
        });
    }
}
