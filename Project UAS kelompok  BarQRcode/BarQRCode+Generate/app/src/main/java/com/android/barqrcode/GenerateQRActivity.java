package com.android.barqrcode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQRActivity extends AppCompatActivity {
    EditText et;
    ImageView img;
    Button btn;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
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
        setContentView(R.layout.activity_generate_qr);
        et = findViewById(R.id.etTextToQR);
        img = findViewById(R.id.imgToQR);
        btn = findViewById(R.id.bGenerateQR);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().length() > 0) {
                    createQR(et.getText().toString());
                }else {
                    Toast.makeText(GenerateQRActivity.this, "No text to generate",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createQR(String inputValue){
// Calculate best resolution for QR
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        // Initializing QR Encoder with your value to be encoded, type you required and Dimension
        qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            img.setImageBitmap(bitmap);
            //qrImage.setScaleType();
        } catch (Exception e) {
            Log.v("BarQRCode", e.toString());
        }
    }
}
