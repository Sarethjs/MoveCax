package dev.movecax.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
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

import dev.movecax.R;

public class ExplorarFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    public ExplorarFragment() {
        // Constructor vacío requerido
    }

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


            // Obtener la ubicación actual y centrar el mapa en ella
            obtenerUbicacionActual();
        }
    }

    private void checkAndRequestLocationPermissions() {
        if (!hasLocationPermission()) {
            // Solicitar permisos de ubicación
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
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi ubicación"));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
                // Manejar la excepción de seguridad aquí
                // Puedes mostrar un mensaje al usuario o tomar otras acciones necesarias.
            }
        }
    }
}
