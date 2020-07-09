package com.android.barqrcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button scanQR, intentGenQR;
    TextView tv;
    String forCopy = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        scanQR = findViewById(R.id.bScan);
        tv = findViewById(R.id.tvResult);
        intentGenQR = findViewById(R.id.bIntentGenQR);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied",forCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this,"Copied to clipboard",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ListenerScanQR(View v) {
        ScanQR();
    }
    public void ListenerIntentGenerateQR(View v) {
        Intent i = new Intent(MainActivity.this, GenerateQRActivity.class);
        startActivity(i);
    }

    private void ScanQR(){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning . . .");
        integrator.addExtra("SCAN_WIDTH",768);
        integrator.addExtra("SCAN_HEIGHT",1024);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
                tv.setText("Found nothing to scan");
            } else {
                Log.e("Scan", "Scanned: " + result.getContents());
                forCopy = result.getContents();

                tv.setText("Scanned result: " + forCopy);
                Toast.makeText(this, "Success scanned\nTap text to copy", Toast.LENGTH_LONG).show();

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
