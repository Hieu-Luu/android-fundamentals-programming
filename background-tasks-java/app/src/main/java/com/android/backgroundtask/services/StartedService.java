package com.android.backgroundtask.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class StartedService extends Service {

    private static final String TAG = "StartedService";
    private Looper serviceLooper;
    private StartedServiceHandler serviceHandler;

    public StartedService() {
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new StartedServiceHandler(serviceLooper);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, TAG + ": service starting, id: " + startId, Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start job and deliver the
        // start ID so we know which request we're stopping when finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        // If we get killed, after returning from here , restart; If the system kills the service after onStartCommand() returns,
        // recreate the service and call onStartCommand(), but do not redeliver the last intent.
        Log.d(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
    }

    // Handler that receives messages from the thread
    public class StartedServiceHandler extends Handler {

        private static final String TAG = "StartedServiceHandler";

        public StartedServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 10 seconds.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
            Log.d(TAG, "Finish work, stop Started Service " + msg.arg1);
        }
    }
}
