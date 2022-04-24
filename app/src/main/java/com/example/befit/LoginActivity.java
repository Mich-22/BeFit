package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void mostrarFormularioRegistro(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}