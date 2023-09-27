package dev.movecax.models;

import java.util.Date;

public class Comment {
    private int id;
    private User user;
    private Route route;
    private Date published;

    // Constructor
    public Comment(int id, User user, Route route, Date published) {
        this.id = id;
        this.user = user;
        this.route = route;
        this.published = published;
    }

    // Métodos getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUsers() {
        return user;
    }

    public void setUsers(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }
}

