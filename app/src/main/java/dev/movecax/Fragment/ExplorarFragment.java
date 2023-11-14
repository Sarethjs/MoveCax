package dev.movecax.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import dev.movecax.Presenters.ExplorePresenter;
import dev.movecax.R;

public class ExplorarFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private ExplorePresenter presenter;
    private Location currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker selectedPlaceMarker;

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

                // Make request
                presenter.makeRequest(currentLocation, location);
            }

            @Override
            public void onError(com.google.android.gms.common.api.Status status) {
                showError("Error: " + status.getStatusMessage());
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

            this.mGoogleMap.setMyLocationEnabled(true);
            this.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            this.checkAndRequestLocationPermissions();
            this.getLocation();
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

    public void showMessage(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
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
        this.currentLocation = userLocation;
    }

    public GoogleMap getmGoogleMap() {
        return mGoogleMap;
    }

    private void buscarUbicacion(String query) {
        Geocoder geocoder = new Geocoder(this.requireContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng location = new LatLng(latitude, longitude);
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(location).title(query));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            }
        } catch (IOException e) {
            this.showError("Error: " + e.getMessage());
        }
    }
}
