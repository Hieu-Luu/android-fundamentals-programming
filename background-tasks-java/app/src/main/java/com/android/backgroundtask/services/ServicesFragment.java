package com.android.backgroundtask.services;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.backgroundtask.R;

import java.util.Objects;

public class ServicesFragment extends Fragment implements View.OnClickListener {

    private ServicesViewModel mViewModel;
    private Intent startedServiceIntent;
    private BoundServiceIBinder mBoundServiceIBinder;
    boolean mBound = false;
    boolean messengerServiceBound = false;
    private Spinner mSpinner;
    private Messenger mService = null;

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.services_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServicesViewModel.class);
        String[] country = {"Started Service", "Intent Service", "Bound Service-IBinder", "Bound Service-Messenger", "Foreground Service"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, country);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getActivity().findViewById(R.id.buttonStartService).setOnClickListener(this);
        mSpinner = getActivity().findViewById(R.id.spinner);
        mSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            Objects.requireNonNull(getContext()).unbindService(connection);
        }
        mBound = false;
        if (messengerServiceBound) {
            Objects.requireNonNull(getContext()).unbindService(mMessengerServiceConnection);
            messengerServiceBound = false;
        }
    }

    private void startStartedService() {

        startedServiceIntent = new Intent(getContext(), StartedService.class);
        Objects.requireNonNull(getActivity()).startService(startedServiceIntent);
    }

    private void boundService() {
        Intent intent = new Intent(getContext(), BoundServiceIBinder.class);
        Objects.requireNonNull(getContext()).bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStartService) {
            switch (mSpinner.getSelectedItem().toString()) {
                case "Started Service":
                    startStartedService();
                    break;
                case "Intent Service":
                    // Start IntentService
                    break;
                case "Bound Service-IBinder":
                    boundService();
                    // Bound service using Binder class
                    // Call a method from the BoundServiceIBind.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    if (mBound) {
                        int num = mBoundServiceIBinder.getRandomNumber();
                        Toast.makeText(getContext(), "number: " + num, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Bound Service-Messenger":
                    Objects.requireNonNull(getContext()).bindService(new Intent(getContext(),
                                    MessengerService.class),
                                    mMessengerServiceConnection,
                                    Context.BIND_AUTO_CREATE);
                    sayHello();
                    break;
                case "Foreground Service":
                    // Start foreground service
                    break;
            }
        }
    }

    /* Defines callbacks for service binding, passed to bindService()*/
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to Service, cast the IBinder and get Service instance
            BoundServiceIBinder.LocalBinder binder = (BoundServiceIBinder.LocalBinder) service;
            mBoundServiceIBinder = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mMessengerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been established, giving us the object we can use to
            // interact with the service.  We are communicating with the service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger((service));
            messengerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            messengerServiceBound = false;
        }
    };

    public void sayHello() {
        if (!messengerServiceBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
