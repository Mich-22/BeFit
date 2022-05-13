package com.example.befit.ui.comida;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.befit.databinding.FragmentComidaBinding;

public class ComidaFragment extends Fragment {

    private FragmentComidaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ComidaViewModel comidaViewModel =
                new ViewModelProvider(this).get(ComidaViewModel.class);

        binding = FragmentComidaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textView20;
        //comidaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}