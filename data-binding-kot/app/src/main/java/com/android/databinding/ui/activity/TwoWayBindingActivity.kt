package com.android.databinding.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModelProviders
import com.android.databinding.BR
import com.android.databinding.R
import com.android.databinding.data.IntervalTimerViewModel
import com.android.databinding.data.IntervalTimerViewModelFactory
import com.android.databinding.databinding.ActivityTwoWayBindingBinding


const val SHARED_PREFS_KEY = "timer"

/**
 * This activity only takes care of binding a ViewModel to the layout. All UI calls are delegated
 * to the Data Binding library or Binding Adapters ([BindingAdapters]).
 *
 * Note that not all calls to the framework are removed, activities are still responsible for non-UI
 * interactions with the framework, like Shared Preferences or Navigation.
 */

class TwoWayBindingActivity : AppCompatActivity() {

    private val intervalTimerViewModel: IntervalTimerViewModel by lazy {
        ViewModelProviders.of(this, IntervalTimerViewModelFactory)
            .get(IntervalTimerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTwoWayBindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_two_way_binding)
        val viewModel = intervalTimerViewModel
        binding.viewModel = intervalTimerViewModel

        /* Save the user setting whenever they change */
        observeAndSaveTimePerSet(
            viewModel.timePerWorkSet, R.string.prefs_timePerWorkSet)
        observeAndSaveTimePerSet(
            viewModel.timePerRestSet, R.string.prefs_timePerRestSet)

        /* Number of sets needs a different  */
        observeAndSaveNumberOfSets(viewModel)
        if (savedInstanceState == null) {
            /* If this is the first run, restore shared settings */
            restorePreferences(viewModel)
            observeAndSaveNumberOfSets(viewModel)
        }
    }

    private fun observeAndSaveTimePerSet(timePerWorkSet: ObservableInt, prefsKey: Int) {
        timePerWorkSet.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                @SuppressLint("CommitPrefEdits")
                override fun onPropertyChanged(observable: Observable?, p1: Int) {
                    Log.d("saveTimePerWorkSet", "Saving time-per-set preference")
                    val sharedPref =
                        getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return
                    sharedPref.edit().apply {
                        putInt(getString(prefsKey), (observable as ObservableInt).get())
                        commit()
                    }
                }
            })
    }

    private fun restorePreferences(viewModel: IntervalTimerViewModel) {
        val sharedPref =
            getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return
        val timePerWorkSetKey = getString(R.string.prefs_timePerWorkSet)
        var wasAnythingRestored = false
        if (sharedPref.contains(timePerWorkSetKey)) {
            viewModel.timePerWorkSet.set(sharedPref.getInt(timePerWorkSetKey, 100))
            wasAnythingRestored = true
        }
        val timePerRestSetKey = getString(R.string.prefs_timePerRestSet)
        if (sharedPref.contains(timePerRestSetKey)) {
            viewModel.timePerRestSet.set(sharedPref.getInt(timePerRestSetKey, 50))
            wasAnythingRestored = true
        }
        val numberOfSetsKey = getString(R.string.prefs_numberOfSets)
        if (sharedPref.contains(numberOfSetsKey)) {
            viewModel.numberOfSets = arrayOf(0, sharedPref.getInt(numberOfSetsKey, 5))
            wasAnythingRestored = true
        }
        if (wasAnythingRestored) Log.d("saveTimePerWorkSet", "Preferences restored")
        viewModel.stopButtonClicked()
    }

    private fun observeAndSaveNumberOfSets(viewModel: IntervalTimerViewModel) {
        viewModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            @SuppressLint("CommitPrefEdits")
            override fun onPropertyChanged(observable: Observable?, p1: Int) {
                if (p1 == BR.numberOfSets) {
                    Log.d("saveTimePerWorkSet", "Saving number of sets preference")
                    val sharedPref =
                        getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return
                    sharedPref.edit().apply {
                        putInt(getString(R.string.prefs_numberOfSets), viewModel.numberOfSets[1])
                        commit()
                    }
                }
            }
        })
    }
}
