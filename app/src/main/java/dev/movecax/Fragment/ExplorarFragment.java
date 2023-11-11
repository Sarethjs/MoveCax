package dev.movecax.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;

import java.util.List;

import dev.movecax.Presenters.ExplorePresenter;
import dev.movecax.R;
import dev.movecax.models.RouteManager;
import dev.movecax.models.RouteRequest;

public class ExplorarFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private ExplorePresenter presenter;
    private  LatLng currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    public ExplorarFragment() {}

    public static ExplorarFragment newInstance() {
        return new ExplorarFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapContainer);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Verificar y solicitar permisos de ubicación
        checkAndRequestLocationPermissions();

        // Add presenter for this fragment
        this.presenter = new ExplorePresenter(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Comprobar si se tienen permisos de ubicación antes de habilitar la capa de ubicación
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true); // Setear la capa de ubicación habilitada
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            obtenerUbicacionActual();
        }
    }

    private void checkAndRequestLocationPermissions() {
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }
    }

    private boolean hasLocationPermission() {
        int fineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void obtenerUbicacionActual() {
        if (hasLocationPermission()) {
            try {
                // Obtener la ubicación actual
                fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        this.currentLocation = new LatLng(latitude, longitude);
                        mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi ubicación"));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

                        LatLng origin = this.getCurrentLocation();
                        LatLng dest = new LatLng(-7, -7);

                        RouteRequest request = new RouteRequest(
                                origin.latitude, origin.longitude,
                                dest.latitude, dest.longitude
                        );

                        RouteManager manager = new RouteManager();
                        manager.getBestRoute(this.presenter, request);
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public GoogleMap getmGoogleMap() {
        return mGoogleMap;
    }

    public void setmGoogleMap(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }
}
