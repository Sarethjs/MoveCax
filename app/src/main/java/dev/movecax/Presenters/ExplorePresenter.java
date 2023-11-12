package dev.movecax.Presenters;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import dev.movecax.Fragment.ExplorarFragment;
import dev.movecax.models.Route;
import dev.movecax.models.RouteManager;
import dev.movecax.models.RouteRequest;

public class ExplorePresenter implements RouteManagerListener {

    private final ExplorarFragment view;

    public ExplorePresenter(ExplorarFragment view) {
        this.view = view;
    }

    public void drawCustomRoute(Route route) {
        Log.d("draw_routes", "drawCustomRoute: Points:");

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(12);
        polylineOptions.color(Color.BLUE);

        // polylineOptions.add(new LatLng(-7.1533782833407695, -78.51512705599512),
        //         new LatLng(-7.157881,-78.508329));
        //

        polylineOptions.addAll(route.getLocations());
        for (LatLng point:
                route.getLocations()) {
            Log.d("draw_routes", "drawCustomRoute: Point: " + point);
        }

        this.view.getmGoogleMap().addPolyline(polylineOptions);
        Log.d("draw_routes", "drawCustomRoute: Polyline added ");
    }

    @Override
    public void routeObtained(String msg, Route route) {
        Log.d("draw_routes", "routeObtained: Route obtained");
        this.drawCustomRoute(route);
    }

    @Override
    public void routNotObtained(String err) {

    }

    public void makeRequest(Location userLocation) {

        LatLng origin = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        LatLng dest = new LatLng(-7, -7);

        RouteRequest request = new RouteRequest(
                origin.latitude, origin.longitude,
                dest.latitude, dest.longitude
        );

        RouteManager manager = new RouteManager();
        manager.getBestRoute(this, request);
    }
}