package com.android.applicationcomponents.service;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.applicationcomponents.R;
import com.android.applicationcomponents.databinding.ServiceDemoFragmentBinding;

public class ServiceDemoFragment extends Fragment {

    private ServiceDemoViewModel mViewModel = new ServiceDemoViewModel();

    public static ServiceDemoFragment newInstance() {
        return new ServiceDemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ServiceDemoFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_demo_fragment, container, false);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceDemoViewModel.class);
        mViewModel.setContext(getContext());
    }
}
