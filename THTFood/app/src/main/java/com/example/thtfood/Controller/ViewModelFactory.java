package com.example.thtfood.Controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;



public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory instance;
    private final Application application;

    private ViewModelFactory(Application application) {
        this.application = application;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (instance == null) {
            instance = new ViewModelFactory(application);
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

