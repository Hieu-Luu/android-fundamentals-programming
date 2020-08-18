package com.android.backgroundtask.asynctask;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.backgroundtask.R;

import java.util.Objects;

public class AsyncTaskFragment extends Fragment implements View.OnClickListener {

    // Key for saving the state of the TextView
    private static final String TEXT_STATE = "currentText";
    private TextView mResultTextView;
    private AsyncTaskViewModel mViewModel;
    private int taskId = 0;

    public static AsyncTaskFragment newInstance() {
        return new AsyncTaskFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.async_task_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(AsyncTaskViewModel.class);
        mResultTextView = getActivity().findViewById(R.id.textViewResult);
        getActivity().findViewById(R.id.buttonStartTask).setOnClickListener(this);
        // Restore TextView if there is a savedInstanceState bundle
        if (savedInstanceState != null) {
            mResultTextView.setText(savedInstanceState.getString(TEXT_STATE));
        }
    }

    private void startTask() {
        mResultTextView.setText(R.string.napping);
        new SimpleAsyncTask(mResultTextView).execute(taskId);
        taskId ++;
    }

    /***
     * Saves the contents of the TextView to restore on configuration change.
     * @param outState he bundle in which the state of the activity is saved when
     * it is spontaneously destroyed.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_STATE, mResultTextView.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStartTask ){
            startTask();
        }
    }
}
