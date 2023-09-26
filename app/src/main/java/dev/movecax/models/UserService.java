package dev.movecax.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("users/create")
    Call<Users> createUser(@Body Users user);
}
