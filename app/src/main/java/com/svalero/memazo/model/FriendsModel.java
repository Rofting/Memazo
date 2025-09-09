package com.svalero.memazo.model;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.FriendshipApiInterface;
import com.svalero.memazo.contract.FriendsContract;
import com.svalero.memazo.domain.FriendshipOutDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsModel implements FriendsContract.Model {

    private final FriendshipApiInterface api =
            ApiClient.createService(FriendshipApiInterface.class);

    @Override
    public void loadFriends(Long userId, OnLoadFriendsListener listener) {
        api.getFriendships(userId, null, null, null)
                .enqueue(new Callback<List<FriendshipOutDto>>() {
                    @Override
                    public void onResponse(Call<List<FriendshipOutDto>> call,
                                           Response<List<FriendshipOutDto>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            listener.onLoadSuccess(resp.body());
                        } else {
                            listener.onLoadError("HTTP " + resp.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FriendshipOutDto>> call, Throwable t) {
                        listener.onLoadError(t.getMessage());
                    }
                });
    }

    @Override
    public void deleteFriendship(long friendshipId, OnDeleteFriendListener listener) {
        api.deleteFriendship(friendshipId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    listener.onDeleteSuccess(friendshipId);
                } else {
                    listener.onDeleteError(friendshipId, "HTTP " + resp.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onDeleteError(friendshipId, t.getMessage());
            }
        });
    }
}
