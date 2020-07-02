package com.android.applicationcomponents.service;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class ServiceDemoViewModel extends ViewModel {
    private Context context;
    public final ObservableField<String> status = new ObservableField<>();

    public Context setContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void startService() {
        Objects.requireNonNull(context).startService(new Intent(context, MyService.class));
    }

    // Method to stop the service
    public void stopService() {
        Objects.requireNonNull(context).stopService(new Intent(context, MyService.class));
    }

}
