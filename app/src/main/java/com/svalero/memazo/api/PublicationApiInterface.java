package com.svalero.memazo.api;

import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PublicationApiInterface {
    @GET("publications")
    Call<List<Publication>> getPublications();

    @POST("publications")
    Call<Publication> addPublication(@Body PublicationInDto publicationDto);

    @GET("users/{id}/publications")
    Call<List<Publication>> getUserPublications(@Path("id") long id);

    @DELETE("publications/{id}")
    Call<Void> deletePublication(@Path("id") long id);

    @PUT("publications/{id}")
    Call<Publication> updatePublication(@Path("id") long id, @Body PublicationInDto publicationDto);


}