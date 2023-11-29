package dev.movecax.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoryService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("history/")
    Call<List<History>> getHistory(@Path("routeId") int userId);

    @POST("history/")
    Call<Void> create(@Body int request);
}
