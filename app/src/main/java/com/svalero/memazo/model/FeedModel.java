package com.svalero.memazo.model;

import android.content.Context;

import androidx.annotation.NonNull; // Asegúrate de importar esto

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.PublicationApiInterface;
import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.domain.Publication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedModel implements FeedContract.Model {

    private Context context;

    public FeedModel(Context context) {
        this.context = context;
    }
    @Override
    public void loadPublications(OnLoadPublicationsListener listener) {
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<List<Publication>> call = api.getPublications();

        call.enqueue(new Callback<List<Publication>>() {
            @Override
            public void onResponse(@NonNull Call<List<Publication>> call, @NonNull Response<List<Publication>> response) {
                if (response.isSuccessful()) {
                    listener.onLoadPublicationsSuccess(response.body());
                } else {
                    listener.onLoadPublicationsError("Error al cargar las publicaciones: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Publication>> call, @NonNull Throwable t) {
                listener.onLoadPublicationsError("Fallo de conexión: " + t.getMessage());
            }

        });
    }
    @Override
    public void deletePublication(long publicationId, OnDeletePublicationListener listener) {
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<Void> call = api.deletePublication(publicationId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // el servidor responde. Comprobamos que la respuesta es exitosa
                if (response.isSuccessful()) {
                    listener.onDeleteSuccess();
                } else {
                    listener.onDeleteError("Error al eliminar la publicación: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                listener.onDeleteError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}