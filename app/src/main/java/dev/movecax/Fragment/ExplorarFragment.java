package dev.movecax.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.movecax.R;

public class ExplorarFragment extends SupportMapFragment {

    public ExplorarFragment() {
        // Constructor vacío requerido
    }

    public static ExplorarFragment newInstance() {
        return new ExplorarFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        // Ajusta las dimensiones del mapa
        int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height); // Define la altura deseada en recursos
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mapHeight));

        // Configura el mapa y realiza las acciones necesarias
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Configura el mapa, agrega marcadores u otras acciones específicas
                LatLng location = new LatLng(37.7749, -122.4194); // Ubicación de San Francisco, por ejemplo.
                googleMap.addMarker(new MarkerOptions().position(location).title("San Francisco"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            }
        });

        return rootView;
    }
}

