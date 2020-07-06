package com.android.databinding.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableInt
import com.android.databinding.R
import com.android.databinding.data.ObservableFieldProfile
import com.android.databinding.databinding.ActivityObservableFieldProfileBinding

class ObservableFieldActivity : AppCompatActivity() {

    private val observableFieldProfile = ObservableFieldProfile("Ada", "Lovelace", ObservableInt(0))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityObservableFieldProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_observable_field_profile)
        binding.userViewModel = observableFieldProfile
    }

    fun onLike(view: View){
        observableFieldProfile.likes.set(observableFieldProfile.likes.get() + 1)
    }
}
