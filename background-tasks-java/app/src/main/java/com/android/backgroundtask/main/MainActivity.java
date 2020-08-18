package com.android.backgroundtask.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.android.backgroundtask.R;
import com.android.backgroundtask.asynctask.AsyncTaskFragment;
import com.android.backgroundtask.asynctaskloader.AsyncTaskLoaderFragment;
import com.android.backgroundtask.broadcasts.BroadcastReceiverFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonAsyncTask).setOnClickListener(this);
        findViewById(R.id.buttonAsyncTaskLoader).setOnClickListener(this);
        findViewById(R.id.buttonCustomBroadcastReceiver).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAsyncTask) {
            loadFragment(AsyncTaskFragment.newInstance());
        }
        if (v.getId() == R.id.buttonAsyncTaskLoader) {
            loadFragment(AsyncTaskLoaderFragment.newInstance());
        }
        if (v.getId() == R.id.buttonCustomBroadcastReceiver) {
            loadFragment(BroadcastReceiverFragment.newInstance());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.commit();
    }
}
