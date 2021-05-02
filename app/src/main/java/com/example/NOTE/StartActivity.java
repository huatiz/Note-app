package com.example.NOTE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Typeface font = Typeface.createFromAsset(getAssets(), "Rajdhani-Regular.ttf");
        TextView text = findViewById(R.id.text);
        text.setTypeface(font);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(800);
                    startActivity(new Intent().setClass(StartActivity.this, MainActivity.class));
                    StartActivity.this.finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}