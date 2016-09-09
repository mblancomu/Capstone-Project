package com.manuelblanco.capstonestage2.model;

import java.io.Serializable;

/**
 * Created by manuel on 17/07/16.
 */
public class Trip implements Serializable{

    private static final long serialVersionUID = -7060210544600464481L;

    private String country;
    private String photo;
    private String type;
    private String id_trip;
    private String description;
    private String rate;
    private String title;
    private String user;
    private String comments;
    private String coords;
    private String routes_id;
    private String hasRoute;
    private String continent;
    private String favorite;
    private String created;
    private String votes;
    private String updated;
    private String voted;

    public Trip() {
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getId_trip() {
        return id_trip;
    }

    public void setId_trip(String id_trip) {
        this.id_trip = id_trip;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getHasRoute() {
        return hasRoute;
    }

    public void setHasRoute(String hasRoute) {
        this.hasRoute = hasRoute;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getRoutes_id() {
        return routes_id;
    }

    public void setRoutes_id(String routes_id) {
        this.routes_id = routes_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
