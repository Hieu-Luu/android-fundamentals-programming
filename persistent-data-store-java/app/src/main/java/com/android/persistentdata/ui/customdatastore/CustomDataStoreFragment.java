package com.android.persistentdata.ui.customdatastore;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.persistentdata.R;

public class CustomDataStoreFragment extends Fragment {

    private CustomDataStoreViewModel mViewModel;

    public static CustomDataStoreFragment newInstance() {
        return new CustomDataStoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_data_store, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CustomDataStoreViewModel.class);
        // TODO: Use the ViewModel
    }

}
