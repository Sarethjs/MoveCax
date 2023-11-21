package dev.movecax.models;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://settling-humpback-clean.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("users/register")
    Call<User> createUser(@Body User user);

    @POST("users/login")
    Call<User> getUser(@Body User user);

    @POST("users/find")
    Call<User> getUserByToken(@Body JsonObject json);

    @POST("users/logout")
    Call<Void> logout(@Body User user);

    @POST("users/password")
    Call<User> changePassword(@Body PasswordChangeRequest request);

    @POST("/users/forgotpass")
    Call<ResponseBody> restorePassword(@Body ResetRequest request);
}
