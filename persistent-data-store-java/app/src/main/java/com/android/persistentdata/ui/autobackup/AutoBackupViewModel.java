package com.android.persistentdata.ui.autobackup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AutoBackupViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AutoBackupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}