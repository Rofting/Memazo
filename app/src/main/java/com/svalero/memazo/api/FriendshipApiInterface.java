package com.svalero.memazo.api;

import com.svalero.memazo.domain.FriendshipOutDto;
import com.svalero.memazo.domain.FriendshipInDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface FriendshipApiInterface {

    @GET("friendships")
    Call<List<FriendshipOutDto>> getFriendships(
            @Query("userId") Long userId,
            @Query("status") String status,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    @POST("friendships")
    Call<FriendshipOutDto> addFriendship(@Body FriendshipInDto inDto);

    @PUT("friendships/{id}")
    Call<FriendshipOutDto> updateFriendship(@Path("id") long id, @Body FriendshipInDto inDto);

    @DELETE("friendships/{id}")
    Call<Void> deleteFriendship(@Path("id") long id);
}
