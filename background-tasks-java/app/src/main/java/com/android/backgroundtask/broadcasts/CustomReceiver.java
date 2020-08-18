package com.android.backgroundtask.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.backgroundtask.BuildConfig;
import com.android.backgroundtask.R;

public class CustomReceiver extends BroadcastReceiver {

    // String constant that defines the custom broadcast Action.
    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";

    /**
     * This callback method gets called when the Broadcast Receiver receives a
     * broadcast that it is registered for.
     *
     * @param context The context in which broadcast receiver is running.
     * @param intent  The broadcast is delivered in the form of an intent which
     *                contains the broadcast action.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if (intentAction != null) {
            String toastMessage = context.getString(R.string.unknown_action);
            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = context.getString(R.string.power_connected);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = context.getString(R.string.power_disconnected);
                    break;
                case Intent.ACTION_HEADSET_PLUG:
                    toastMessage = context.getString(R.string.headphone_plug);
                    break;
                case ACTION_CUSTOM_BROADCAST:
                    toastMessage = context.getString(R.string.custom_broadcast_toast);
                    break;
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
