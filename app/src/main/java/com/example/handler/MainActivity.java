package com.example.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    ImageView img;
    Button bt;
    Bitmap bitmap = null;
    ProgressDialog progressDialog;
    String url ="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4HtmaPWDFwVXLtxyl5FNQ9jN9z-ahaSAO3moQnfzlgsIpNp0UM3229XnQQnjfpFQ1hlg&usqp=CAU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textview);
        img = findViewById(R.id.imageView3);
        bt = findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(MainActivity.this, "Dang Download", "Dang download");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        bitmap = getPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4HtmaPWDFwVXLtxyl5FNQ9jN9z-ahaSAO3moQnfzlgsIpNp0UM3229XnQQnjfpFQ1hlg&usqp=CAU");
                        Message msg = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        String thMessage = "Download xong";
                        bundle.putString("message", thMessage);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                };
                Thread th = new Thread(runnable);
                th.start();
            }
        });
    }

    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handlerMessage(Message msg){
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String message = bundle.getString("message");
            tv.setText(message);
            img.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    };

    private Bitmap getPicture(String link){
        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}