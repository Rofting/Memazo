package com.svalero.memazo.model;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.PublicationApiInterface;
import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.domain.Publication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedModel implements FeedContract.Model {

    @Override
    public void loadPublications(OnLoadPublicationsListener listener) {
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<List<Publication>> call = api.getPublications();

        call.enqueue(new Callback<List<Publication>>() {
            @Override
            public void onResponse(Call<List<Publication>> call, Response<List<Publication>> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Error al cargar las publicaciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Publication>> call, Throwable t) {
                listener.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}