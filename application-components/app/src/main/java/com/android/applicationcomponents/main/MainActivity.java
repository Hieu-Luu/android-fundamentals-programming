package com.android.applicationcomponents.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.android.applicationcomponents.R;

public class MainActivity extends AppCompatActivity {

    private  MainViewModel mainViewModel = new MainViewModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setContext(this);
        setContentView(R.layout.activity_main);
    }
}
