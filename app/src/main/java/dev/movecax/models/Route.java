package dev.movecax.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

    private  final String routeName;
    private final List<LatLng> points;

    public Route(String routeName, List<LatLng> points) {
        this.routeName = routeName;
        this.points = points;
    }

    public List<LatLng> getPoints() {
        return points;
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
