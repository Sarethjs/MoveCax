package dev.movecax.models;

public class RouteRequest {
    private final double lato;
    private final double lono;
    private final double latd;
    private final double lond;

    public RouteRequest(double lato, double lono, double latd, double lond) {
        this.lato = lato;
        this.lono = lono;
        this.latd = latd;
        this.lond = lond;
    }
}
