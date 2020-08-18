package com.android.backgroundtask.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class BoundServiceIBinder extends Service {

    private static final String TAG = "BoundServiceIBinder";
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    // Random number generator
    private final Random mGenerator = new Random();

    public BoundServiceIBinder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return  binder;
    }

    /* method for clients*/
    public int getRandomNumber() {
        return mGenerator.nextInt();
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        BoundServiceIBinder getService() {
            // Return this instance of LocalService so clients can call public methods
            return BoundServiceIBinder.this;
        }
    }
}
