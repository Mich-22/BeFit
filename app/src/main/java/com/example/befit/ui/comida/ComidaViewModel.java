package com.example.befit.ui.comida;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComidaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ComidaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No hay pedidos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}