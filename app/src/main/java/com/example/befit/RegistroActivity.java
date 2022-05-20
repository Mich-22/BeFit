package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RegistroActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView txtNombre;
    TextView txtApellido;
    TextView txtDomicilio;
    TextView  txtTelefono;
    TextView txtEmail;
    TextView  txtPassword;
    private static final String TAG = "RegistroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = FirebaseFirestore.getInstance();

        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtApellido = (TextView) findViewById(R.id.txtApellido);
        txtDomicilio = (TextView) findViewById(R.id.txtDomicilio);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPassword = (TextView) findViewById(R.id.txtPassword);


    }
    public void mostrarPantallaPrincipal(View view) {
        // Create a new user with a first and last name
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", txtNombre.getText().toString());
        userData.put("apellido", txtApellido.getText().toString());
        userData.put("domicilio", txtDomicilio.getText().toString());
        userData.put("telefono", txtTelefono.getText().toString());
        userData.put("email", txtEmail.getText().toString());
        userData.put("password", txtPassword.getText().toString());


        // Add a new document with a generated ID
        db.collection("usuarios")
            .document(txtEmail.getText().toString())
            .set(userData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(RegistroActivity.this, "Bienvenid@ " + txtNombre.getText().toString(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistroActivity.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Error adding document", e);
                }
            });

        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm backgroundThreadRealm = Realm.getInstance(config);
        UserModel userModel = new UserModel(txtNombre.getText().toString(), txtApellido.getText().toString(),
                txtTelefono.getText().toString(), txtEmail.getText().toString());
        backgroundThreadRealm.executeTransaction (transactionRealm -> {
            transactionRealm.insert(userModel);
        });

        Intent intent = new Intent( RegistroActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}