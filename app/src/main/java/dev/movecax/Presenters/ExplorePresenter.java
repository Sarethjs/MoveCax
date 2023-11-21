package dev.movecax.Presenters;

import android.graphics.Color;
import android.location.Location;

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
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(12);
        polylineOptions.color(Color.BLUE);
        polylineOptions.addAll(route.getPoints());
        this.view.getmGoogleMap().addPolyline(polylineOptions);
    }

    @Override
    public void routeObtained(String msg, Route route) {
        this.drawCustomRoute(route);
        this.view.showRouteInformation(route);
        this.view.showMessage("Mejor ruta encontrada");
    }

    @Override
    public void routeNotObtained(String err) {
        this.view.showError(err);
    }

    public void makeRequest(Location userLocation, LatLng destination) {

        this.view.showMessage("Calculando mejor ruta");

        LatLng origin = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

        RouteRequest request = new RouteRequest(
                origin.latitude, origin.longitude,
                destination.latitude, destination.longitude
        );

        RouteManager manager = new RouteManager();
        manager.getBestRoute(this, request);
    }
}