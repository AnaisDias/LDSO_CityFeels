package com.example.ana.cityfeels.sia;

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

    public static JSONObject get(String url) {
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

            return new JSONObject(contentBuilder.toString());
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }

}
