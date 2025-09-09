package com.svalero.memazo.contract;

import com.svalero.memazo.domain.FriendshipOutDto;
import java.util.List;

public interface FriendsContract {

    interface View {
        void showFriendships(List<FriendshipOutDto> friendships);
        void showError(String message);

        void onFriendDeleted(long friendshipId);
        void onFriendDeleteError(long friendshipId, String message);
    }

    interface Presenter {
        void loadFriends(Long userId);
        void detach();

        void deleteFriendship(long friendshipId);
    }

    interface Model {
        interface OnLoadFriendsListener {
            void onLoadSuccess(List<FriendshipOutDto> friendships);
            void onLoadError(String error);
        }
        interface OnDeleteFriendListener {
            void onDeleteSuccess(long friendshipId);
            void onDeleteError(long friendshipId, String error);
        }

        void loadFriends(Long userId, OnLoadFriendsListener listener);

        void deleteFriendship(long friendshipId, OnDeleteFriendListener listener);
    }
}
