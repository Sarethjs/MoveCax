package dev.movecax.Presenters;

import dev.movecax.models.Route;

public interface RouteManagerListener {
    void routeObtained(String msg, Route route);
    void routNotObtained(String err);
}
