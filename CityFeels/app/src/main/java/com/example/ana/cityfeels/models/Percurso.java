package com.example.ana.cityfeels.models;

public class Percurso {

    private int id;
    private int[] pointsOfInterestIds;
    private int lastPointIndex = 0;

    public Percurso(int id, int[] pointsOfInterestIds) {
        this.id = id;
        this.pointsOfInterestIds = pointsOfInterestIds;
    }

    public int getId() {
        return this.id;
    }

    public int getStartingPointId() {
        return pointsOfInterestIds[0];
    }

    public int getEndingPointId() {
        return this.pointsOfInterestIds[this.pointsOfInterestIds.length - 1];
    }

    public int[] getPointsOfInterestIds() {
        return this.pointsOfInterestIds;
    }

    public boolean isAtStart() {
        return this.lastPointIndex == 0;
    }

    public boolean isAtEnd() {
        return this.lastPointIndex >= this.pointsOfInterestIds.length - 1;
    }

    public int getLastPointId() {
        return this.pointsOfInterestIds[this.lastPointIndex];
    }

    public int nextPoint() {
        if(isAtEnd())
            return -1;
        else
            return this.pointsOfInterestIds[++this.lastPointIndex];
    }

}
