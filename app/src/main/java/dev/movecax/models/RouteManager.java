package dev.movecax.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.airbnb.lottie.L;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import dev.movecax.Presenters.RouteManagerListener;
import dev.movecax.models.helpers.ErrorFormatter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteManager {

    private final RouteService service = RouteService.retrofit.create(RouteService.class);

    public void getBestRoute(RouteManagerListener listener, RouteRequest request) {

        Call<ResponseBody> call = service.getBestRoute(request.getLato(), request.getLono(),
                request.getLatd(), request.getLond());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {

                List<LatLng> routePoints = new ArrayList<>();

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    try (JsonReader jsonReader = new JsonReader(
                            new InputStreamReader(responseBody.byteStream(),
                                    StandardCharsets.UTF_8))) {

                        jsonReader.beginArray();

                        int counter = 0;
                        Log.d("route_parser", "onResponse: Reading Json File");
                        while (jsonReader.hasNext()) {
                            Log.d("route_parser", "onResponse: Read: " + counter + " point");
                            try {
                                routePoints.add(readPoint(jsonReader));
                            } catch (IllegalStateException e) {
                                break;
                            }
                            counter++;
                        }
                        // jsonReader.endArray();
                        jsonReader.close();
                        Log.d("route_parser", "onResponse: File read!");
                        Log.d("route_parser", "onResponse: Points read: " + counter);
                        listener.routeObtained("Route loaded",
                                new Route("None", routePoints));
                    }
                    catch (JsonSyntaxException e) {
                        listener.routNotObtained("Error reading response");
                        e.printStackTrace();
                    }
                    catch (IOException | NullPointerException e) {
                        listener.routNotObtained("Error reading response");
                    }

                } else {
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.routNotObtained(error);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                listener.routNotObtained("No se  puede conectar al servidor");
                Log.d("route_parser", "onFailure: " + t.getMessage());
            }
        });


    }

    private LatLng readPoint(JsonReader reader) throws IOException {

        double lat = 0;
        double lon = 0;

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("lat")) {
                lat = reader.nextDouble();
            } else if (name.equals("lon")) {
                lon = reader.nextDouble();
            }  else {
                reader.skipValue();
            }
        }

        reader.endObject();
        return new LatLng(lat, lon);
    }
}
