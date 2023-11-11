package dev.movecax.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RouteService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("routes/")
    Call<Route> getBestRoute(@Body RouteRequest request);
}
