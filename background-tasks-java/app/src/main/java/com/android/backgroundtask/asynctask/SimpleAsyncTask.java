package com.android.backgroundtask.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * AsyncTask is impractical for some use cases:
 *
 * - Changes to device configuration cause problems.
 * - Old AsyncTask objects stay around, and your app may run out of memory or crash.
 *
 * When to use AsyncTask:
 *
 * - Short or interruptible com.android.backgroundtask.ui.tasks.
 * - Tasks that don't need to report back to UI or user.
 * - Low-priority com.android.backgroundtask.ui.tasks that can be left unfinished.
 *
 */
public class SimpleAsyncTask extends AsyncTask<Integer, Void, String> {

    // The TextView where showing results
    private WeakReference<TextView> mTextView;

    // Constructor that provides a reference to the TextView from the MainActivity


    public SimpleAsyncTask(TextView tv) {
        mTextView = new WeakReference<>(tv);
    }

    /**
     * @param params Pass the task id
     * @return Returns the string including the amount of time that the background thread sleep
     */
    @Override
    protected String doInBackground(Integer... params) {

        Random r = new Random();
        int n = r.nextInt(11);
        // Make the task take long enough that we have time to rotate the phone while it is running
        int s = n * 200;
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Returns a String result
        return "Task " + params[0] + ": Awake at last after sleeping for " + s + " milliseconds!";
    }

    @Override
    protected void onPostExecute(String result) {
        mTextView.get().setText(result);
    }
}
