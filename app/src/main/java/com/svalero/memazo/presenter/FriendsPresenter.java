package com.svalero.memazo.presenter;

import com.svalero.memazo.contract.FriendsContract;
import com.svalero.memazo.domain.FriendshipOutDto;
import com.svalero.memazo.model.FriendsModel;
import java.util.List;

public class FriendsPresenter implements FriendsContract.Presenter,
        FriendsContract.Model.OnLoadFriendsListener,
        FriendsContract.Model.OnDeleteFriendListener {

    private FriendsContract.View view;
    private FriendsContract.Model model;

    public FriendsPresenter(FriendsContract.View view) {
        this.view = view;
        this.model = new FriendsModel();
    }

    @Override
    public void loadFriends(Long userId) {
        model.loadFriends(userId, this);
    }

    @Override
    public void detach() {
        view = null;
    }

    // Delete
    @Override
    public void deleteFriendship(long friendshipId) {
        model.deleteFriendship(friendshipId, this);
    }

    // Callbacks
    @Override
    public void onLoadSuccess(List<FriendshipOutDto> friendships) {
        if (view != null) view.showFriendships(friendships);
    }

    @Override
    public void onLoadError(String error) {
        if (view != null) view.showError(error);
    }

    @Override
    public void onDeleteSuccess(long friendshipId) {
        if (view != null) view.onFriendDeleted(friendshipId);
    }

    @Override
    public void onDeleteError(long friendshipId, String error) {
        if (view != null) view.onFriendDeleteError(friendshipId, error);
    }
}
