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

    public void setContext(Context context) {
        this.context = context;
    }

    public void startService() {
        Objects.requireNonNull(context).startService(new Intent(context, MyService.class));
    }

    public void stopService() {
        Objects.requireNonNull(context).stopService(new Intent(context, MyService.class));
    }

}
