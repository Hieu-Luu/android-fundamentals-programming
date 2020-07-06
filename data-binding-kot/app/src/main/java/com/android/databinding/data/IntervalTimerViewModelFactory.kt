package com.android.databinding.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.databinding.util.DefaultTimer
import java.lang.IllegalArgumentException

/**
 * Factory for ViewModels
 * */
object IntervalTimerViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IntervalTimerViewModel::class.java)) {
            return IntervalTimerViewModel(DefaultTimer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}