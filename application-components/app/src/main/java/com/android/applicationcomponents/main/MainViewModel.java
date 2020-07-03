package com.android.applicationcomponents.main;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.android.applicationcomponents.service.ServiceDemoActivity;

public class MainViewModel extends ViewModel {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void startActivityDemoActivity() {

    }

    public void startServiceDemoActivity() {
        Intent intent = new Intent(context, ServiceDemoActivity.class);
        context.startActivity(intent);
    }

    public void startBroadcastReceiverDemoActivity() {

    }

    public void startContentProviderDemoActivity() {

    }
}
