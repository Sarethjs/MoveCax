package dev.movecax.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.L;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.movecax.Presenters.ExplorePresenter;
import dev.movecax.R;

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
            this.mGoogleMap.setMyLocationEnabled(true);
            this.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            this.checkAndRequestLocationPermissions();
            getLocation();

            /*

            Log.d("draw_routes", "onMapReady: ");
            // this.presenter.drawCustomRoute();

             */
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

    private void getLocation() {
        if (this.hasLocationPermission()) {
            try {
                // Get user location
                this.fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        this.setMarkerOnMap(location);
                        this.userLocationChanged(location);
                    } else {
                        this.showError("No se pudo obtener tu ubicación");
                    }
                });
            } catch (SecurityException e) {
                this.showError("No se pudo obtener tu ubicación");
            }
        }
    }

    public void showError(String error) {
        Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void setMarkerOnMap(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        final LatLng userLocation = new LatLng(lat, lon);

        this.mGoogleMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Mi ubicación")
        );
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
    }

    private void userLocationChanged(Location userLocation) {
        this.presenter.makeRequest(userLocation);
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
