package dev.movecax.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import dev.movecax.R;

public class ExplorarFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker selectedPlaceMarker;

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

        // Inicializar la API de Places
        Places.initialize(requireContext(), "AIzaSyCYHrWYHo23QvaemjicBIQkBoI1_WUW_C4");

        // Obtener una referencia a la barra de búsqueda
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarUbicacion(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Puedes realizar acciones mientras el texto de búsqueda cambia
                if (newText.isEmpty()) {
                    // Limpiar marcador si no hay texto en la búsqueda
                    if (selectedPlaceMarker != null) {
                        selectedPlaceMarker.remove();
                        selectedPlaceMarker = null;
                    }
                }
                return true;
            }
        });

        // Configurar el AutocompleteSupportFragment
        autocompleteFragment = AutocompleteSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.autocomplete_fragment, autocompleteFragment);
        transaction.commit();

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteFragment.setCountry("PE"); // Cambia a tu país
        autocompleteFragment.setHint("¿A dónde quieres llegar?");
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.OVERLAY);
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng location = place.getLatLng();
                mGoogleMap.clear();
                selectedPlaceMarker = mGoogleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            }

            @Override
            public void onError(com.google.android.gms.common.api.Status status) {
                Toast.makeText(requireContext(), "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Verificar y solicitar permisos de ubicación
        checkAndRequestLocationPermissions();

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
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
            }
        }
    }

    private void buscarUbicacion(String query) {
        Geocoder geocoder = new Geocoder(requireContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng location = new LatLng(latitude, longitude);

                mGoogleMap.clear(); // Limpiar marcadores previos
                mGoogleMap.addMarker(new MarkerOptions().position(location).title(query));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


