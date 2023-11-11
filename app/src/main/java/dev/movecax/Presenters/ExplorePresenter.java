package dev.movecax.Presenters;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import dev.movecax.Fragment.ExplorarFragment;
import dev.movecax.models.Route;

public class ExplorePresenter implements RouteManagerListener{

    private final ExplorarFragment view;

    public ExplorePresenter(ExplorarFragment view) {
        this.view = view;
    }

    public void drawCustomRoute(Route route) {
        List<LatLng> points = route.getLocations();
        PolylineOptions polyline = new PolylineOptions();
        polyline.color(Color.BLUE);
        polyline.width(10);
        polyline.addAll(points);
        this.view.getmGoogleMap().addPolyline(polyline);
    }

    @Override
    public void routeObtained(String msg, Route route) {
        this.drawCustomRoute(route);
    }

    @Override
    public void routNotObtained(String err) {

    }
}