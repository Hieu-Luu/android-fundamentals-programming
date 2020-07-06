package com.android.databinding.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.databinding.R
import com.android.databinding.data.ProfileLiveDataViewModel
import com.android.databinding.data.ProfileObservableViewModel
import com.android.databinding.databinding.ActivityViewmodelProfileBinding

class ViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain ViewModel from ViewModelProviders
        val liveViewModel = ViewModelProviders.of(this).get(ProfileLiveDataViewModel::class.java)

        // An alternative ViewModel using Observable fields and @Bindable properties can be used:
         val observableViewModel = ViewModelProviders.of(this).get(ProfileObservableViewModel::class.java)

        //Obtain binding
        val binding: ActivityViewmodelProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_viewmodel_profile)

        // Binding layout with ViewModel
        binding.viewModel = liveViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this
    }
}
