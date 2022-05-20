package com.example.befit.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.befit.R;
import com.example.befit.UserModel;
import com.example.befit.databinding.FragmentSlideshowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    FirebaseFirestore db;
    TextView txtNombre;
    TextView txtApellido;
    TextView txtTelefono;
    TextView txtEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        db = FirebaseFirestore.getInstance();

        txtNombre = (TextView) root.findViewById(R.id.txtNombre_upd);
        txtApellido = (TextView) root.findViewById(R.id.txtApellido_upd);
        txtTelefono = (TextView) root.findViewById(R.id.txtTelefono_upd);
        txtEmail = (TextView) root.findViewById(R.id.txtEmail_upd);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm backgroundThreadRealm = Realm.getInstance(config);
        backgroundThreadRealm.executeTransaction (transactionRealm -> {
            UserModel user = transactionRealm.where(UserModel.class).findFirst();
            txtNombre.setText(user.getName());
            txtApellido.setText(user.getApellido());
            txtTelefono.setText(user.getTelefono());
            txtEmail.setText(user.getEmail());
        });

        Button btn = (Button) root  .findViewById(R.id.buttonUpd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatos();
            }
        });
        return root;
    }


    public void actualizarDatos() {
        // Create a new user with a first and last name
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", txtNombre.getText().toString());
        userData.put("apellido", txtApellido.getText().toString());
        userData.put("telefono", txtTelefono.getText().toString());
        userData.put("email", txtEmail.getText().toString());

        // Add a new document with a generated ID
        db.collection("usuarios")
                .document(txtEmail.getText().toString())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                    }
                });

        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm backgroundThreadRealm = Realm.getInstance(config);
        backgroundThreadRealm.executeTransaction (transactionRealm -> {
            UserModel user = transactionRealm.where(UserModel.class).findFirst();
            user.setName(txtNombre.getText().toString());
            user.setApellido(txtApellido.getText().toString());
            user.setTelefono(txtTelefono.getText().toString());
            user.setEmail(txtEmail.getText().toString());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}