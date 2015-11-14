package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.JsonHttpRequest;
import com.example.ana.cityfeels.sia.Location;
import com.example.ana.cityfeels.sia.PointOfInterest;

import org.json.JSONException;
import org.json.JSONObject;

public class SIA {

    private static final String WEB_SERVICE_URL = "https://database-sia.herokuapp.com";
    private static final String WEB_SERVICE_POI_URL = WEB_SERVICE_URL + "/pontos";

    private SIA() {}

    public static PointOfInterest getPointOfInterest(Location location) throws JSONException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?lat=%s&long=%s", location.latitude, location.longitude);

        JSONObject response = JsonHttpRequest.get(url + queryString);

        String about = response.getString("Informacao");
        return new PointOfInterest(about);
    }
}
