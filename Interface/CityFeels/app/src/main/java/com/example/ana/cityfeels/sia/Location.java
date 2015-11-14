package com.example.ana.cityfeels.sia;

public class Location {

    public float latitude;
    public float longitude;

    public Location() {}

    public Location(float latitude, float longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return this.latitude + ", " + this.longitude;
    }

}
