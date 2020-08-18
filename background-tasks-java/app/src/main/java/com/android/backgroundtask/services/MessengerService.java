package com.android.backgroundtask.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MessengerService extends Service {

    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;
    /**
     * Target we publish for clients to send messages to IncomingHandler
     */
    Messenger mMessenger;

    public MessengerService() {
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }

    /**
     * Handler of incoming messages fro clients.
     */
    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_SAY_HELLO) {
                Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
            } else {
                super.handleMessage(msg);
            }
        }
    }
}
