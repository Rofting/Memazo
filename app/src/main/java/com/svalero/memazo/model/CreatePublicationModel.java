package com.svalero.memazo.model;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.PublicationApiInterface;
import com.svalero.memazo.contract.CreatePublicationContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePublicationModel implements CreatePublicationContract.Model {

    @Override
    public void createPublication(PublicationInDto publicationDto, OnCreatePublicationListener listener) {
        // Obtenemos la interfaz de la API y preparamos la llamada
        PublicationApiInterface api = ApiClient.getRetrofitService(PublicationApiInterface.class);
        Call<Publication> call = api.addPublication(publicationDto);

        // Ejecutamos la llamada
        call.enqueue(new Callback<Publication>() {
            @Override
            public void onResponse(Call<Publication> call, Response<Publication> response) {
                if (response.isSuccessful()) {
                    // Si todo va bien, notificamos al Presenter
                    listener.onSuccess();
                } else {
                    // Si hay un error, notificamos al Presenter
                    listener.onError("Error al publicar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Publication> call, Throwable t) {
                // Si hay un fallo de conexión, notificamos al Presenter
                listener.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}