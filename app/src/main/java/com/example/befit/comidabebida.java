package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.befit.ui.comida.ComidaFragment;

public class comidabebida extends AppCompatActivity {
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        setContentView(R.layout.fragment_comida);
    }

    public void mostrarBebidas(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, BebidaActivity.class);
        startActivity(intent);
    }
}