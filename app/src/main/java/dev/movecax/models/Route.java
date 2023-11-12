package dev.movecax.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private  final String routeName;
    private List<LatLng> points;

    public Route(String routeName, List<LatLng> points) {
        this.routeName = routeName;
        this.points = points;
    }

    public void addLocation(LatLng location) {
        this.points.add(location);
    }

    public String getRouteName() {
        return routeName;
    }

    public List<LatLng> getLocations() {
        return points;
    }

    public void setLocations(List<LatLng> locations) {
        this.points = locations;
    }

    @NonNull
    @Override
    public String toString() {
        return "Route{" +
                "routeName='" + routeName + '\'' +
                ", locations=" + points +
                '}';
    }
}
