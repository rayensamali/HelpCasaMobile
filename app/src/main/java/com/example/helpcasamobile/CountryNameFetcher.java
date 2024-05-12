package com.example.helpcasamobile;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class CountryNameFetcher extends AsyncTask<Void,Void,ArrayList<String>> {

    private static final String API_URL = "https://restcountries.com/v3.1/all?fields=name";
    private ArrayList<String> countryNames = new ArrayList<>();
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(API_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            String countriesJsonStr = buffer.toString();

            JSONArray countriesArray = new JSONArray(countriesJsonStr);
            for (int i = 0; i < countriesArray.length(); i++) {
                JSONObject countryObject = countriesArray.getJSONObject(i);
                JSONObject nameObject = countryObject.getJSONObject("name");
                String commonName = nameObject.getString("common");
                countryNames.add(commonName);
            }
            return countryNames;

        } catch (IOException | JSONException e) {
            Log.e("CountryNameFetcher", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("CountryNameFetcher", "Error closing stream", e);
                }
            }
        }
    }
}
