package dev.movecax.models;

import java.util.Date;

public class History {

    private final String email, routeName;
    private String dest, origin;
    private Date date;

    public History(String email, String routeName, String dest) {
        this.email = email;
        this.routeName = routeName;
    }



}
