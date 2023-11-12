package dev.movecax.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RouteService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("routes/")
    Call<ResponseBody> getBestRoute(@Query("lato") double latOrigin,
                                    @Query("lono") double lonOrigin,
                                    @Query("latd") double latDest,
                                    @Query("lond") double lonDest);
}
