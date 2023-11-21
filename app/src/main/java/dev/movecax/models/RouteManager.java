package dev.movecax.models;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.movecax.Presenters.RouteManagerListener;
import dev.movecax.models.helpers.ErrorFormatter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteManager {

    private static final String POINTS_ATTRIBUTE = "points";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String PRICE_ATTRIBUTE = "price";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";

    private final RouteService service = RouteService.retrofit.create(RouteService.class);

    public void getBestRoute(@NonNull RouteManagerListener listener, @NonNull RouteRequest request) {

        Call<ResponseBody> call = service.getBestRoute(request.getLato(), request.getLono(),
                request.getLatd(), request.getLond());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {

                String routeName = "None";
                float price = 0;
                List<LatLng> routePoints = new ArrayList<>();

                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {

                        try (JsonReader jsonReader = new JsonReader(
                                new InputStreamReader(Objects.requireNonNull(responseBody).byteStream(),
                                        StandardCharsets.UTF_8))) {

                            jsonReader.beginObject();

                            Log.d("route_parser", "onResponse: Reading Json File");
                            while (jsonReader.hasNext()) {
                                String attribute = jsonReader.nextName();

                                switch (attribute) {
                                    case RouteManager.NAME_ATTRIBUTE:
                                        routeName = jsonReader.nextString();
                                        break;
                                    case RouteManager.PRICE_ATTRIBUTE:
                                        price = (float) jsonReader.nextDouble();
                                        break;
                                    case RouteManager.POINTS_ATTRIBUTE:
                                        routePoints = readPoints(jsonReader);
                                        break;
                                    default:
                                        jsonReader.skipValue();
                                        break;
                                }
                            }
                            jsonReader.endObject();
                            listener.routeObtained("", new Route(
                                    routeName,
                                    RouteManager.optimizeRoute(request, routePoints),
                                    price
                            ));
                            Log.d("route_parser", "onResponse: Parsed Json File");
                        } catch (IOException | NullPointerException e) {
                            listener.routeNotObtained("Error reading response");
                            Log.d("route_parser", "onResponse: Error: " + e);
                        }
                    }

                } else {
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.routeNotObtained(error);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                listener.routeNotObtained("No se  puede conectar al servidor");
                Log.d("route_parser", "onFailure: " + t.getMessage());
            }
        });
    }

    private List<LatLng> readPoints(JsonReader reader) throws IOException {

        List<LatLng> points = new ArrayList<>();
        reader.beginArray();

        while (reader.hasNext()) {
            reader.beginObject();
            double lat = 0, lon = 0;

            while (reader.hasNext()) {
                final String attribute = reader.nextName();

                switch (attribute) {
                    case RouteManager.LATITUDE:
                        lat = reader.nextDouble();
                        break;

                    case RouteManager.LONGITUDE:
                        lon = reader.nextDouble();
                        break;

                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            points.add(new LatLng(lat, lon));
        }

        reader.endArray();
        return points;
    }

    private static List<LatLng> optimizeRoute(RouteRequest request, List<LatLng> points) {

        LatLng origin = new LatLng(request.getLato(), request.getLono());
        LatLng dest = new LatLng(request.getLatd(), request.getLond());

        int indexToOrigin = 0, indexToDest = 0;
        float minDistOrigin = Float.MAX_VALUE, minDistDest = Float.MAX_VALUE;

        for (int index = 0; index < points.size(); index++) {
            float distToOrigin = RouteManager.calculateDistance(origin, points.get(index));
            float distToDest = RouteManager.calculateDistance(dest, points.get(index));

            if (distToOrigin < minDistOrigin) {
                minDistOrigin = distToOrigin;
                indexToOrigin = index;
            }

            if (distToDest < minDistDest) {
                minDistDest = distToDest;
                indexToDest = index;
            }
        }

        return new ArrayList<>((indexToOrigin <= indexToDest)
                ? points.subList(indexToOrigin, indexToDest + 1)
                : points.subList(indexToDest, indexToOrigin + 1)
        );
    }

    private static float calculateDistance(LatLng point1, LatLng point2) {
        Location location1 = new Location("Point 1");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);

        Location location2 = new Location("Point 2");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);

        return location1.distanceTo(location2);
    }
}
