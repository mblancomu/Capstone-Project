package com.manuelblanco.capstonestage2.model;

/**
 * Created by manuel on 15/08/16.
 */
public class AroundMe implements Comparable{

    private double distance;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    private Trip trip;

    public AroundMe() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Object o) {
        double compareDistance=((AroundMe)o).getDistance();
        /* For Ascending order*/
        return (int) (this.distance-compareDistance);

    }
}
