package com.android.persistentdata.ui.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.persistentdata.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SharedPreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SharedPreferencesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COUNT_KEY = "count";
    private static final String COLOR_KEY = "color";

    private String mParam1;
    private String mParam2;

    private int mCount = 0;
    private int mColor;
    private TextView mShowCountTextView;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.android.persistentdata.ui.sharedpreferences";

    public SharedPreferencesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SharedPreferencesFragment.
     */
    public static SharedPreferencesFragment newInstance() {
        SharedPreferencesFragment fragment = new SharedPreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared_preferences, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).findViewById(R.id.black_background_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeBackGround(v);
                    }
                });
        getActivity().findViewById(R.id.blue_background_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeBackGround(v);
                    }
                });
        getActivity().findViewById(R.id.red_background_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeBackGround(v);
                    }
                });
        getActivity().findViewById(R.id.green_background_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeBackGround(v);
                    }
                });
        getActivity().findViewById(R.id.count_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countUp();
                    }
                });
        getActivity().findViewById(R.id.reset_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reset();
                    }
                });

        mShowCountTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.count_textview);
        mColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.design_default_color_background);
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

        // Restore preferences
        mCount = mPreferences.getInt(COUNT_KEY, 0);
        mShowCountTextView.setText(String.format("%s", mCount));
        mColor = mPreferences.getInt(COUNT_KEY, mColor);
        mShowCountTextView.setBackgroundColor(mColor);
    }

    public void changeBackGround(View view) {
        int color = ((ColorDrawable) view.getBackground()).getColor();
        mShowCountTextView.setBackgroundColor(color);
        mColor = color;
    }

    public void countUp() {
        mCount++;
        mShowCountTextView.setText(String.format("%s", mCount));
    }

    public void reset() {
        mCount = 0;
        mShowCountTextView.setText(String.format("%s", mCount));
        mColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.design_default_color_background);
        mShowCountTextView.setBackgroundColor(mColor);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(COUNT_KEY, mCount);
        editor.putInt(COLOR_KEY, mColor);
        editor.apply();
    }
}
