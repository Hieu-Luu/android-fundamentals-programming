package com.android.applicationcomponents.service;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.applicationcomponents.R;
import com.android.applicationcomponents.databinding.ServiceFragmentBinding;

public class ServiceDemoFragment extends Fragment {

    private ServiceDemoViewModel mViewModel;

    public static ServiceDemoFragment newInstance() {
        return new ServiceDemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceDemoViewModel.class);
        mViewModel.setContext(getContext());
    }
}
