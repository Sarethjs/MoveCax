package dev.movecax.models;

import java.util.Date;

public class History {

    private String routeName;
    private String origin;
    private String destination;
    private Date dateTime;

    public History(String routeName, String origin, String destination, Date dateTime) {
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.dateTime = dateTime;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
