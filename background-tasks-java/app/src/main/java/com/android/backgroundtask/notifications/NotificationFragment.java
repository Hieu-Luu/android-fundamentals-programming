package com.android.backgroundtask.notifications;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.backgroundtask.R;
import com.android.backgroundtask.main.MainActivity;

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";

    // Constant for the notification actions buttons
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.android.backgroundtask.notifications.ACTION_UPDATE_NOTIFICATION";
    // Notification channel ID
    public static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    // Notification ID
    private static final int NOTIFICATION_ID = 0;
    private Button buttonNotify;
    private Button buttonCancel;
    private Button buttonUpdate;
    private NotificationManager mNotifyManager;
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private NotificationViewModel mViewModel;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        createNotificationChannel();
        getActivity().registerReceiver(mReceiver,
                new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        buttonNotify = getActivity().findViewById(R.id.notify);
        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        buttonUpdate = getActivity().findViewById(R.id.update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });
        buttonCancel = getActivity().findViewById(R.id.cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
        setNotificationButtonState(true, false, false);
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null){
            Log.d(TAG, "Unregister the NotificationReceiver!");
            getActivity().unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    /** Creates a Notification channel, for OREO or higher*/
    public void createNotificationChannel() {

        // Create a notification manager object
        mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification channels are only available in OREO and higher. So, add a check on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Create the NotificationChannel with all the parameters
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Channel name",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Channel description");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification() {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(getContext(),
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifyBuilder =  getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_update_black_24dp,
                "Updating", updatePendingIntent);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, true, true);
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        // Set up the pending intent that is delivered when the notification is clicked
        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getContext(),
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.
                Builder(getContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle("Title")
                .setContentText("Text")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    public void updateNotification() {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Updating"));
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, false, true);
    }

    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
        // Reset the buttons.
        setNotificationButtonState(true, false, false);
    }

    void setNotificationButtonState(Boolean isNotifyEnabled, Boolean isUpdateEnabled,
                                    Boolean isCancelEnabled) {
        buttonNotify.setEnabled(isNotifyEnabled);
        buttonUpdate.setEnabled(isUpdateEnabled);
        buttonCancel.setEnabled(isCancelEnabled);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }
}
