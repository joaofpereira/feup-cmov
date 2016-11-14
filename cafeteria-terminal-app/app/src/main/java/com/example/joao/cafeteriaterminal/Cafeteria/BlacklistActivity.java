package com.example.joao.cafeteriaterminal.Cafeteria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.cafeteriaterminal.QRCODE.ReaderActivity;
import com.example.joao.cafeteriaterminal.R;
import com.google.gson.Gson;

public class BlacklistActivity extends AppCompatActivity {

    BlackListUser blacklistUser;

    TextView message;
    Button backToScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        Gson gson = new Gson();

        String trans_S = getIntent().getStringExtra("blacklistUser");
        blacklistUser = gson.fromJson(trans_S, BlackListUser.class);

        message = (TextView) findViewById(R.id.message);
        backToScan = (Button) findViewById(R.id.back_to_scan);

        message.setText(blacklistUser.getMessage());

        backToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ReaderActivity.class);
                startActivity(intent);
            }
        });
    }
}
