package com.example.befit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class MainActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        setContentView(R.layout.activity_main);
        goToLogin();

        Realm.init(getApplicationContext());
    }



    public void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RealmConfiguration config = new RealmConfiguration.Builder()
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();
                Realm realm = Realm.getInstance(config);
                RealmQuery<UserModel> usersQuery = realm.where(UserModel.class);
                long usuariosEncontrados = usersQuery.count();
                if (usuariosEncontrados > 0)
                    startActivity(welcomeIntent);
                else startActivity(loginIntent);
            }
        }, 1000);

    }
}