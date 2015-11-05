package com.example.ana.cityfeels;

import android.location.LocationListener;
import android.os.Bundle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LocationEventDispatcher {

    private static List<LocationEventListener> LISTENERS = new LinkedList<>();

    /*
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            String lat = String.format("%f", location.getLatitude());
            String lon = String.format("%f", location.getLongitude());
            String coord = lat + " " + lon;
            coords.setText(coord);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };
    */

    // So it's impossible to instantiate
    private LocationEventDispatcher() {}

    public static boolean registerOnNewLocation(LocationEventListener listener)
    {
        return LISTENERS.add(listener);
    }

    public static void fireNewLocation(Location location)
    {
        Iterator<LocationEventListener> it = LISTENERS.iterator();
        while(it.hasNext())
        {
            it.next().onNewLocation(location);
        }
    }

}
