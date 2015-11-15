package com.example.ana.cityfeels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonHttpRequest {

    private JsonHttpRequest() {}

    public static JSONObject getObject(String url) throws IOException, JSONException {
        return new JSONObject(getContent(url));
    }

    public static JSONArray getArray(String url) throws IOException, JSONException {
        return new JSONArray(getContent(url));
    }

    private static String getContent(String url) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            URL urlInstance = new URL(url);
            urlConnection = (HttpURLConnection) urlInstance.openConnection();
            InputStreamReader inputReader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedInputReader = new BufferedReader(inputReader);
            StringBuilder contentBuilder = new StringBuilder();

            String line;
            while((line = bufferedInputReader.readLine()) != null)
            {
                contentBuilder.append(line);
            }

            bufferedInputReader.close();
            urlConnection.disconnect();

            return contentBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

}
