package dev.movecax.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dev.movecax.Info.BottomSheetFragment;
import dev.movecax.Presenters.ExplorePresenter;
import dev.movecax.R;
import dev.movecax.models.Route;

public class ExplorarFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private ExplorePresenter presenter;
    private Location currentLocation;
    private Location destination;
    private String streetDest, streetOrigin;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker selectedPlaceMarker;
    private BottomSheetFragment bottomSheetFragment;

    public ExplorarFragment() {
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

        // Add presenter for this fragment
        this.presenter = new ExplorePresenter(this);
        // Inicializar la API de Places
        Places.initialize(requireContext(), "AIzaSyCYHrWYHo23QvaemjicBIQkBoI1_WUW_C4");

        // Obtener una referencia a la barra de búsqueda
        searchView = rootView.findViewById(R.id.searchView);

        // Configurar el AutocompleteSupportFragment
        autocompleteFragment = AutocompleteSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.autocomplete_fragment, autocompleteFragment);
        transaction.commit();

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteFragment.setCountry("PE"); // Cambia a tu país
        autocompleteFragment.setHint("¿A dónde quieres llegar?");
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.OVERLAY);


        // Establecer la ubicación de Cajamarca como límite para las búsquedas
        LatLng cajamarcaLatLng = new LatLng(-7.1638, -78.5003); // Coordenadas de Cajamarca
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(cajamarcaLatLng, cajamarcaLatLng));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng location = place.getLatLng();
                mGoogleMap.clear();
                selectedPlaceMarker = mGoogleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

                // Make request
                presenter.makeRequest(currentLocation, location);
                destination = new Location("Destination");
                destination.setLatitude(location.latitude);
                destination.setLongitude(location.longitude);
                streetDest = place.getName();
            }

            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                showError("Error: " + status.getStatusMessage());
            }
        });

        // Verify and request permissions to access location
        this.checkAndRequestLocationPermissions();

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
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

    public void showRouteInformation(Route route) {

        // Get Street name from origin
        this.streetOrigin = this.getStreetName(new LatLng(
                this.currentLocation.getLatitude(),
                this.currentLocation.getLongitude()
        ));

        // Getting street name
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(
                route, this.streetDest, this.streetOrigin
        );
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }

    private String getStreetName(@NonNull LatLng coordinates) {

        Geocoder geocoder;

        if (this.getContext() != null) {
            geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        } else return null;

        try {
            List<Address> addresses = geocoder.getFromLocation(
                    coordinates.latitude,
                    coordinates.longitude, 1);
            if (addresses != null)
                return addresses.get(0).getThoroughfare();
            else
                return null;
        } catch (IOException | NullPointerException e){
            return null;
        }
    }

    private void drawFromOrToDest() {


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}

