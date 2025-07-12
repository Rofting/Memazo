package com.svalero.memazo.api;

import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PublicationApiInterface {

    /**
     * Obtiene una lista de todas las publicaciones.
     */
    @GET("publications")
    Call<List<Publication>> getPublications(); // Devuelve una lista de la clase 'Publication'

    /**
     * Envía los datos para crear una nueva publicación.
     * @param publicationDto El objeto DTO con los datos de la nueva publicación.
     */
    @POST("publications")
    Call<Publication> addPublication(@Body PublicationInDto publicationDto); // Envía un 'PublicationInDto'
}