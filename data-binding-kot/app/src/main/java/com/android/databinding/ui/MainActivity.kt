package com.android.databinding.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.databinding.R
import com.android.databinding.data.SimpleViewModel
import com.android.databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Obtain ViewModel from ViewModelProviders, the viewmo
    private val viewModel by lazy { ViewModelProvider(this).get(SimpleViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}
