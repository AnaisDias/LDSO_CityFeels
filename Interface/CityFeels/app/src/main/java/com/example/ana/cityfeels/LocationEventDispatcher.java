package com.example.ana.cityfeels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LocationEventDispatcher {

    private static List<LocationEventListener> LISTENERS = new LinkedList<>();

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
