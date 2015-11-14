package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.Location;
import com.example.ana.cityfeels.sia.PointOfInterest;
import com.example.ana.cityfeels.sia.Route;

import org.json.JSONException;

public class DataSource {

    private DataSource() {}

    public enum DataLayer {
        Basic, Local, Detailed, Another
    }

    private static PointOfInterest LAST_POINT_OF_INTEREST = null;

    public static PointOfInterest getPointOfInterest(Location location, DataLayer layer) {
        return DataSource.getPointOfInterest(location, layer, null);
    }

    public static PointOfInterest getPointOfInterest(Location location, DataLayer layer, Route route) {
        try {
            PointOfInterest poi = SIA.getPointOfInterest(location);
            return poi == null ? poi : (LAST_POINT_OF_INTEREST = poi);
        } catch (JSONException e) {
            return null;
        }
    }

    public static PointOfInterest getLastPointOfInterest() {
        return LAST_POINT_OF_INTEREST;
    }

}
