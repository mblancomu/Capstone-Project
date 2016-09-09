package com.manuelblanco.capstonestage2.model;

/**
 * Created by manuel on 15/08/16.
 */
public class Coordinates {

    private String id;
    private String type;
    private String latitude;
    private String longitude;

    public Coordinates() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
