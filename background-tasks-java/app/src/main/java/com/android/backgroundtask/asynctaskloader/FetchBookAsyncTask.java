package com.android.backgroundtask.asynctaskloader;

import android.os.AsyncTask;
import android.widget.TextView;

import com.android.backgroundtask.R;
import com.android.backgroundtask.internetconnection.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBookAsyncTask extends AsyncTask<String, Void, String> {

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    public FetchBookAsyncTask(WeakReference<TextView> mTitleText, WeakReference<TextView> mAuthorText) {
        this.mTitleText = mTitleText;
        this.mAuthorText = mAuthorText;
    }

    @Override
    protected String doInBackground(String... queries) {
        return NetworkUtils.getBookInfo(queries[0]);
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

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
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            } else {
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }
        } catch (Exception e) {
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
            e.printStackTrace();
        }
    }
}
