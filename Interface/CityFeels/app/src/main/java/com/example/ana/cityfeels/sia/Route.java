package com.example.ana.cityfeels.sia;

public class Route {

    public int id;
    public Location startingLocation;
    public Location endingLocation;

    public Route() {}

    public Route(int id, Location startingLocation, Location endingLocation)
    {
        this.id = id;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
    }

}
