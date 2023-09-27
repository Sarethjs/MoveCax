package dev.movecax.models;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("users/create")
    Call<User> createUser(@Body User user);

    @GET("users/login")
    Call<User> getUser(@Body User user);
}
