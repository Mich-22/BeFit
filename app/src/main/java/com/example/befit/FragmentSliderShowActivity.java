package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FragmentSliderShowActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        setContentView(R.layout.activity_main);
        goToLogin();

    }



    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 1000);

    }

    public void mostrarPerfil(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}