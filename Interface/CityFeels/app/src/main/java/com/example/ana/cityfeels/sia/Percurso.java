package com.example.ana.cityfeels.sia;

import com.example.ana.cityfeels.Location;

public class Percurso {

    public int id;
    public Location startingLocation;
    public Location endingLocation;

    public Percurso() {}

    public Percurso(int id, Location startingLocation, Location endingLocation)
    {
        this.id = id;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
    }

}
