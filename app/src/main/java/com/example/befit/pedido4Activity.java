
package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class pedido4Activity extends AppCompatActivity {
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido4);

    }

    public void mostrarTarjeta(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, TarjetaActivity.class);
        startActivity(intent);
    }
}