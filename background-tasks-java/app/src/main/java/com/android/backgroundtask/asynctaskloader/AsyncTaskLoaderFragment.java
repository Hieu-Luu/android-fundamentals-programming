package com.android.backgroundtask.asynctaskloader;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.backgroundtask.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * The WhoWroteIt app queries the Book Search API for books based
 * on a user's search.  It uses an AsyncTask to run the search task in
 * the background.
 */
public class AsyncTaskLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private AsyncTaskLoaderViewModel mViewModel;
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;
    private Button mSearchButton;

    public static AsyncTaskLoaderFragment newInstance() {
        return new AsyncTaskLoaderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.async_loader_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AsyncTaskLoaderViewModel.class);
        mBookInput = Objects.requireNonNull(getActivity()).findViewById(R.id.bookInput);
        mTitleText = getActivity().findViewById(R.id.titleText);
        mAuthorText = getActivity().findViewById(R.id.authorText);
        mSearchButton = getActivity().findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBooks(mSearchButton);
            }
        });

//        if (getActivity().getSupportLoaderManager().getLoader(0) != null) {
//            getActivity().getSupportLoaderManager().initLoader(0, null, this);
//        }
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    private void searchBooks(View view) {
        String queryString = mBookInput.getText().toString();
        InputMethodManager inputManager = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() & queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
            mAuthorText.setText("");
            mTitleText.setText(R.string.loading);
        } else {
            mAuthorText.setText("");
            if (queryString.length() == 0) {
                mTitleText.setText(R.string.no_search_term);
            } else {
                mTitleText.setText(R.string.no_network);
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new BookLoader(Objects.requireNonNull(getActivity()), queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i = 0;
            String title = null;
            String authors = null;

            while (i < itemsArray.length() && authors == null && title == null) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeINfo = book.getJSONObject("volumeInfo");

                try {
                    title = volumeINfo.getString("title");
                    authors = volumeINfo.getString("authors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i++;
            }
            if (title != null && authors != null) {
                mTitleText.setText(title);
                mAuthorText.setText(authors);
            } else {
                mTitleText.setText(R.string.no_results);
                mAuthorText.setText("");
            }
        } catch (Exception e) {
            mTitleText.setText(R.string.no_results);
            mAuthorText.setText("");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
