package dev.movecax.models;

import java.util.Date;

public class Comment {
    private int id;
    private Users users;
    private Route route;
    private Date published;

    // Constructor
    public Comment(int id, Users users, Route route, Date published) {
        this.id = id;
        this.users = users;
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

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
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

