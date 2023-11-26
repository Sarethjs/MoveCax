package dev.movecax.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

    private  final String routeName;
    private final List<LatLng> points;
    private final float price;

    public Route(String routeName, List<LatLng> points, float price) {
        this.routeName = routeName;
        this.points = points;
        this.price = price;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public float getPrice() {
        return price;
    }

    public String getRouteName() {
        return routeName;
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
