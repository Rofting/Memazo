package com.svalero.memazo.api;

import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiInterface {

    @GET("users")
    Call<List<User>> getUsers();

    @POST("users")
    Call<User> addUser(@Body User user);

    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);

    @PUT("users/{id}")
    Call<User> modifyUser(@Path("id") long id, @Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") long id);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") long id);

    @GET("users/{id}/publications")
    Call<List<Publication>> getUserPublications(@Path("id") long id);

}
