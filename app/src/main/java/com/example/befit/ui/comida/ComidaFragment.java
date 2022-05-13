package com.example.befit.ui.comida;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.befit.BebidaActivity;
import com.example.befit.databinding.FragmentComidaBinding;

public class ComidaFragment extends Fragment {

    private FragmentComidaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ComidaViewModel comidaViewModel =
                new ViewModelProvider(this).get(ComidaViewModel.class);

        binding = FragmentComidaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final ImageButton btnGoToBebidas = binding.imageButton2;
        btnGoToBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BebidaActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}