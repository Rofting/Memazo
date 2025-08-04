package com.svalero.memazo.model;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.PublicationApiInterface;
import com.svalero.memazo.api.UserApiInterface;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileModel implements ProfileContract.Model {
    @Override
    public void loadUser(long userId, ProfileContract.Model.OnUserLoadedListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
        Call<User> call = api.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    listener.onUserLoaded(response.body());
                } else {
                    listener.onUserError("Error al cargar el usuario: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onUserError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
    @Override
    public void loadUserPublications(long userId, ProfileContract.Model.OnPublicationsLoadedListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
    Call<List<Publication>> call = api.getUserPublications(userId);
    call.enqueue(new Callback<List<Publication>>() {
        @Override
        public void onResponse(Call<List<Publication>> call, Response<List<Publication>> response) {
            if (response.isSuccessful()) {
                listener.onPublicationsLoaded(response.body());
                } else {
                listener.onPublicationsError("Error al cargar las publicaciones: " + response.code());
            }
        }
        @Override
        public void onFailure(Call<List<Publication>> call, Throwable t) {
            listener.onPublicationsError("Fallo de conexión: " + t.getMessage());
        }
    });
    }
}
