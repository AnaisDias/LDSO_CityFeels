package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.PontoInteresse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SIA {

    private static final String WEB_SERVICE_URL = "https://database-sia.herokuapp.com";
    private static final String WEB_SERVICE_POI_URL = WEB_SERVICE_URL + "/pontos";

    private SIA() {}

    public static PontoInteresse getPointOfInterest(Location location) throws JSONException, IOException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?lat=%s&long=%s", location.latitude, location.longitude);

        JSONObject response = JsonHttpRequest.getObject(url + queryString);

        if(response == null)
            return null;
        else
        {
            String about = response.getString("informacao");
            String detAbout = response.getString("infdetalhada");
            return new PontoInteresse(location, about, detAbout);
        }
    }

    public static PontoInteresse[] getCloseByPointsOfInterest(Location location, float distanceInMeters) throws IOException, JSONException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?lat=%s&long=%s", location.latitude, location.longitude);
        queryString += "&distance=" + distanceInMeters;

        JSONArray results = JsonHttpRequest.getArray(url + queryString);
        if(results == null)
            return null;
        else
        {
            PontoInteresse[] closeByPoints = new PontoInteresse[results.length()];
            for(int i = 0; i < results.length(); i++)
            {
                JSONObject currentPoint = results.getJSONObject(i);
                String infdetalhada = currentPoint.getString("infdetalhada");
                String informacao = currentPoint.getString("informacao");
                double latitude = currentPoint.getDouble("latitude");
                double longitude = currentPoint.getDouble("longitude");
                closeByPoints[i] = new PontoInteresse(new Location(latitude, longitude), informacao, infdetalhada);
            }

            return closeByPoints;
        }
    }
}
