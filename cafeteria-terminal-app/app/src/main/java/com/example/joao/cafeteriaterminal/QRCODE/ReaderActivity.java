package com.example.joao.cafeteriaterminal.QRCODE;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.joao.cafeteriaterminal.API.CafeteriaRestTerminalUsage;
import com.example.joao.cafeteriaterminal.Cafeteria.Transaction;
import com.example.joao.cafeteriaterminal.R;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.*;

import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();

                try {
                    Transaction transaction = CafeteriaRestTerminalUsage.createTransaction(result.getContents());

                    Toast.makeText(this,"UUID " + transaction.getUserID().toString(),Toast.LENGTH_LONG).show();

                    for(int i = 0; i < transaction.getProducts().length(); i++) {
                        //following two lines are equivalent... whichever is easier for you...
                        Log.i("products:", transaction.getProducts().get(i).toString());
                    }
                    Toast.makeText(this,"date: " + transaction.getDate().toString(),Toast.LENGTH_LONG).show();

                    RequestParams params = new RequestParams();

                    params.put("userID", transaction.getUserID());
                    params.put("date", transaction.getDate());

                    /*Gson gson = new Gson();
                    JsonElement element = gson.toJsonTree(transaction.getProductIDList(), new TypeToken<List<Pair<Integer,Integer>>>() {}.getType());

                    if (! element.isJsonArray()) {
                                // fail appropriately
                        throw new JSONException("Failed to convert to json array");
                    }

                    JsonArray jsonArray = element.getAsJsonArray();
                    Log.i("testing json array",jsonArray.toString());*/
                    Log.i("Products content:", transaction.getProducts().toString() );
                    params.put("products", transaction.getProducts());


                    try {

                        CafeteriaRestTerminalUsage.confirmTransaction(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
