package com.svalero.memazo.model;

import android.content.Context;
import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.PublicationApiInterface;
import com.svalero.memazo.api.UserApiInterface;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.db.AppDatabase;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.domain.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileModel implements ProfileContract.Model {

    private Context context;

    public ProfileModel(Context context) {
        this.context = context;
    }

    @Override
    public void loadUser(long userId, OnUserLoadedListener listener) {
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
                listener.onUserError("Fallo de conexión (usuario): " + t.getMessage());
            }
        });
    }

    @Override
    public void loadUserPublications(long userId, OnPublicationsLoadedListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
        Call<List<Publication>> call = api.getUserPublications(userId);
        call.enqueue(new Callback<List<Publication>>() {
            @Override
            public void onResponse(Call<List<Publication>> call, Response<List<Publication>> response) {
                if (response.isSuccessful()) {
                    listener.onPublicationsLoaded(response.body());
                } else {
                    listener.onPublicationsError("Error al cargar publicaciones: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Publication>> call, Throwable t) {
                listener.onPublicationsError("Fallo de conexión (publicaciones): " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFavorites(OnFavoritesLoadedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            List<FavoritePublication> favorites = db.favoritePublicationDao().getAll();
            listener.onFavoritesLoaded(favorites);
        });
    }

    @Override
    public void deletePublication(long publicationId, OnDeletePublicationListener listener) {
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<Void> call = api.deletePublication(publicationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onDeletePublicationSuccess();
                } else {
                    listener.onDeletePublicationError("Error al borrar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onDeletePublicationError("Fallo de conexión (borrado): " + t.getMessage());
            }
        });
    }

    @Override
    public void updatePublication(long publicationId, PublicationInDto publicationDto, OnUpdatePublicationListener listener) {
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<Publication> call = api.updatePublication(publicationId, publicationDto);

        call.enqueue(new Callback<Publication>() {
            @Override
            public void onResponse(Call<Publication> call, Response<Publication> response) {
                if (response.isSuccessful()) {
                    listener.onUpdatePublicationSuccess(response.body());
                } else {
                    listener.onUpdatePublicationError("Error al actualizar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Publication> call, Throwable t) {
                listener.onUpdatePublicationError("Fallo de conexión (actualización): " + t.getMessage());
            }
        });
    }

    @Override
    public void updateUser(long userId, User user, OnUserUpdatedListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
        Call<User> call = api.modifyUser(userId, user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    listener.onUserUpdated(response.body());
                } else {

                    listener.onUserUpdateError("Error al actualizar el usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onUserUpdateError("Fallo de conexión (usuario): " + t.getMessage());
            }
        });
    }
}