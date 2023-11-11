package dev.movecax.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.movecax.Presenters.RouteManagerListener;
import dev.movecax.models.helpers.ErrorFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteManager {

    private final RouteService service = RouteService.retrofit.create(RouteService.class);

    public void getBestRoute(RouteManagerListener listener, RouteRequest request) {
        Call<Route> call = service.getBestRoute(request);
        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(@NonNull Call<Route> call,
                                   @NonNull Response<Route> response) {

                if (response.isSuccessful()) {

                        if (response.body() != null) {

                            Route bestRoute = response.body();
                            Log.d("route_parser", "onResponse: Response: " + bestRoute.getRouteName());
                            Log.d("route_parser", "onResponse: Response: " + bestRoute.getLocations());
                            listener.routeObtained("Ruta: " + bestRoute.getRouteName(), bestRoute);
                        }
                        else {
                            listener.routNotObtained("No data received from server");
                            Log.d("route_parser", "onResponse: No data received from server");
                        }
                } else {
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.routNotObtained(error);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Route> call,
                                  @NonNull Throwable t) {
                listener.routNotObtained("No se  puede conectar al servidor");
                Log.d("route_parser", "onFailure: " + t.getMessage());
            }
        });


    }
}
