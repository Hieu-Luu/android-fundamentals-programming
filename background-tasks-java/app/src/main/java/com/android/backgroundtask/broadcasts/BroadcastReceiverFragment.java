package com.android.backgroundtask.broadcasts;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.backgroundtask.BuildConfig;
import com.android.backgroundtask.R;

public class BroadcastReceiverFragment extends Fragment {

    private BroadcastReceiverViewModel mViewModel;
    private CustomReceiver myReceiver = new CustomReceiver();

    // String constant that defines the custom broadcast Action.
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";

    public static BroadcastReceiverFragment newInstance() {
        return new BroadcastReceiverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.broadcast_receiver_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BroadcastReceiverViewModel.class);

        // Defines the IntentFilter
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);

        if (null != getContext()) {
            getContext().registerReceiver(myReceiver, filter);

            // Register the receiver to receive custom broadcast
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter(ACTION_CUSTOM_BROADCAST));
        }
        getActivity().findViewById(R.id.sendBroadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomBroadcast();
            }
        });
    }

    /**
     * Click event handler for the button, that sends custom broadcast using the LocalBroadcastManager.
     */
    public void sendCustomBroadcast() {
        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        if (null != getContext()){
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(customBroadcastIntent);
        }
    }

    @Override
    public void onDestroy() {
        if (null != getContext()){
            getContext().unregisterReceiver(myReceiver);
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }
}
