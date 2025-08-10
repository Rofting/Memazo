package com.svalero.memazo.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.UserApiInterface;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.db.AppDatabase;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileModel implements ProfileContract.Model {

    // 1. Añadimos una variable para guardar el contexto
    private Context context;

    // 2. Creamos un constructor para recibir el contexto
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
                listener.onUserError("Fallo de conexión: " + t.getMessage());
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
                    listener.onPublicationsError("Error al cargar las publicaciones: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Publication>> call, Throwable t) {
                listener.onPublicationsError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFavorites(OnFavoritesLoadedListener listener) {
        // 3. Las operaciones de base de datos DEBEN ejecutarse en un hilo secundario
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Obtenemos la instancia de la base de datos
            AppDatabase db = AppDatabase.getInstance(context);
            // Realizamos la consulta para obtener todos los favoritos
            List<FavoritePublication> favorites = db.favoritePublicationDao().getAll();

            // 4. Devolvemos el resultado al hilo principal para actualizar la UI
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onFavoritesLoaded(favorites);
            });
        });
    }
}