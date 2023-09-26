package com.example.duantotnghiep.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.duantotnghiep.MainActivity;
import com.example.duantotnghiep.R;

public class SplashScreen_Activity extends AppCompatActivity {

    int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen_Activity.this, Login_Activity.class );
                startActivity(intent);

                finish();
            }
        },SPLASH_TIME_OUT);
    }
}