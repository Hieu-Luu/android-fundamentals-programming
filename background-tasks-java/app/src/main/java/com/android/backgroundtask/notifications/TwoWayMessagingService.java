//package com.android.backgroundtask.notifications;
//
//import android.app.Activity;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.Messenger;
//import android.os.RemoteException;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.android.backgroundtask.R;
//
//import java.util.ArrayList;
//
//public class TwoWayMessagingService extends Service {
//    /** For showing and hiding notification */
//    NotificationManager mNM;
//    /** Keeps track of all current registered clients */
//    static ArrayList<Messenger> mClients = new ArrayList<>();
//    /** Hold last value set by client */
//    static int mValue = 0;
//    /** Command to service to registered a client, receiving callbacks from the service. The Message's replyTo field must be a
//     * Messenger of the client where callbacks should be sent.*/
//    public static final int MSG_REGISTER_CLIENT = 1;
//    /** Command to the service to unregister a client, ot stop receiving callbacks from the service. The Message's replyTo field
//     * must be a Messenger of the client as previously given with MSG_REGISTER_CLIENT.
//     */
//    public static final int MSG_UNREGISTERED_CLIENT = 2;
//    /** Command to service to set a new value.  This can be sent to the service to supply a new value, and will be sent by the service
//     *  to any registered clients with the new value.
//     */
//    public static final int MSG_SET_VALUE = 3;
//
//    /** Handler of incoming messages from clients*/
//    static class IncomingHandler extends Handler {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case MSG_REGISTER_CLIENT:
//                    mClients.add(msg.replyTo);
//                    break;
//                case MSG_UNREGISTERED_CLIENT:
//                    mClients.remove(msg.replyTo);
//                    break;
//                case MSG_SET_VALUE:
//                    mValue = msg.arg1;
//                    for (int i = mClients.size()-1; i >=0; i --) {
//                        try {
//                            mClients.get(i).send(Message.obtain(null,
//                                    MSG_SET_VALUE, mValue, 0    ));
//                        } catch (RemoteException e) {
//                            // The client is dead.  Remove it from the list; we are going through the list from back to front
//                            // so this is safe to do inside the loop.
//                            mClients.remove(i);
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }
//        }
//    }
//
//    /** Target we publish for clients to send messages to IncomingHandler */
//    final Messenger mMessenger = new Messenger(new IncomingHandler());
//
//    @Override
//    public void onCreate() {
//        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Display a notification about us starting
//        showNotification();
//    }
//
//    @Override
//    public void onDestroy() {
//        // Cancel the persistent notification
//        mNM.cancel(R.string.remote_service_start);
//        // Tell the user service stopped
//        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
//        super.onDestroy();
//    }
//
//    public TwoWayMessagingService() {
//    }
//
//    /**
//     * When binding to the service, we return an interface to our messenger for sending messages to the service.
//     */
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mMessenger.getBinder();
//    }
//
//    /** Show a notification while this service is running */
//    private void showNotification() {
//        // In this sample, we'll use the same text for the ticker and expand notification
//        CharSequence text = getText(R.string.remote_service_started);
//
//        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, Controller.class));
//
//        // Set the info for the views that show in the notification panel.
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setTicker(text)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(getText(R.string.local_service_label))
//                .setContentText(text)
//                .setContentIntent(contentIntent)
//                .build();
//        // Send notification. We use a string id because it is a unique number. We use it later to cancle.
//        mNM.notify(R.string.remote_service_started, notification);
//    }
//
//    /**
//     * <p>Example of explicitly starting and stopping the remove service.
//     * This demonstrates the implementation of a service that runs in a different
//     * process than the rest of the application, which is explicitly started and stopped
//     * as desired.</p>
//     *
//     * <p>Note that this is implemented as an inner class only keep the sample
//     * all together; typically this code would appear in some separate class.
//     */
//    public static class Controller extends Activity {
//        @Override
//        protected void onCreate(@Nullable Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//            setContentView(R.layout.remote_service_controller);
//            Button button = (Button)findViewById(R.id.start);
//            button.setOnClickListener(mStartListener);
//            button = (Button)findViewById(R.id.stop);
//            button.setOnClickListener(mStopListener);
//        }
//
//        private View.OnClickListener mStartListener = new View.OnClickListener() {
//            public void onClick(View v) {
//                // Make sure the service is started.  It will continue running
//                // until someone calls stopService().
//                // We use an action code here, instead of explictly supplying
//                // the component name, so that other packages can replace
//                // the service.
//                startService(new Intent(Controller.this, RemoteService.class));
//            }
//        };
//
//        private View.OnClickListener mStopListener = new View.OnClickListener() {
//            public void onClick(View v) {
//                // Cancel a previous call to startService().  Note that the
//                // service will not actually stop at this point if there are
//                // still bound clients.
//                stopService(new Intent(Controller.this, RemoteService.class));
//            }
//        };
//
//    }
//}
