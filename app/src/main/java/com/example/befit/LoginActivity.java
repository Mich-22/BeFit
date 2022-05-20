package com.example.befit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //region Facebook properties
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    LoginButton loginButton;
    //endregion

    //region Google properties
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final  int RC_SIGN_IN = 9001;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private FirebaseAuth mAuth;
    //endregion
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        //region Facebook init
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                String asd = loginResult.toString();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                String holaaa = response.toString();
                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    guardarUsusarioEnFirestore(name, email);


                                    RealmConfiguration config = new RealmConfiguration.Builder()
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
                                    Realm backgroundThreadRealm = Realm.getInstance(config);
                                    UserModel userModel = new UserModel(name, "",
                                            "", email);
                                    backgroundThreadRealm.executeTransaction (transactionRealm -> {
                                        transactionRealm.insert(userModel);
                                    });

                                    Toast.makeText(LoginActivity.this, "Bienvenid@ " + name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                } catch (Exception ex){

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                String error = exception.toString();
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                // App code
            }
        });

        //endregion

        //region Google init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        statusTextView = (TextView) findViewById(R.id.status_textview);
        signInButton = (SignInButton) findViewById(R.id.btngmail);
        signInButton.setOnClickListener(this);


/*
        signOutButton = (Button) findViewById(R.id.sign_Out_Button);
        signOutButton.setOnClickListener(this);*/

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("306683492977-40hj10ntarvpgvon2e4bmc21r7f5spsb.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        //endregion
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        //region Facebook result
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //endregion

        //region Google result
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSingInResult(result);
        }
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        Log.d(TAG, "Got ID token.");
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
        }
        //endregion
    }

    //region Google methods

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btngmail:
                signIn();


        }
    }

    public void  handleSingInResult(GoogleSignInResult result){
        Log.d(TAG, "HandleSignInResult:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            statusTextView.setText("Hello, "   + acct.getDisplayName());
        } else{

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,  "onConnectionFailed" +  connectionResult);
    }


    public void signOut(){
        /*Auth.GoogleSignInApi.signOut (mGoogleApiClient).setResultCallback(new ResultCallback<Status>(){
            @Override
                    public void onResult(@NonNull Status status){
                    statusTextView.setText("Signed out");
            }
        });*/
    }
    //endregion

    public void mostrarFormularioRegistro(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void guardarUsusarioEnFirestore(String nombre, String email){

        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("email", email);
        userData.put("apellido", "");
        userData.put("domicilio", "");
        userData.put("telefono", "");
        userData.put("password", "");


        // Add a new document with a generated ID
        db.collection("usuarios")
                .document(email.toString())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LoginActivity.this, "Bienvenid@ " + nombre, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}