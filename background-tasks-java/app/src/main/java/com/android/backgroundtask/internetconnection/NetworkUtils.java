package com.android.backgroundtask.internetconnection;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    // Constants for the various components of the Books API request.
    // Base endpoint URL for the Books API.
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    // Searching parameters
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";

    public static String getBookInfo(String queryString) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String boolJsonString = null;

        try {
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            // Convert the URI tp URL
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the inputStream
            InputStream inputStream = urlConnection.getInputStream();
            //Create buffered reader from that input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Use StringBuilder to hold the incoming response
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string
                builder.append(line);

                // Since this is JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            boolJsonString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Write the final JSON response to the log
        Log.d(LOG_TAG, boolJsonString != null ? boolJsonString : "respond empty");
        return boolJsonString;
    }

}
