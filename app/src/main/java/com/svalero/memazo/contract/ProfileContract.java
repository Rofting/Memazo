package com.svalero.memazo.contract;

import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;

import java.util.List;

public interface ProfileContract {
    interface View {
        void showProfileData(User user);
        void showPublicationList(List<Publication> publications);
        void showError(String message);
        void showFavoritePublications(List<FavoritePublication> favorites);
    }
    interface Presenter {
        void loadUserProfile(long userId);
        void loadFavorites();
    }
    interface Model {
        void loadFavorites(OnFavoritesLoadedListener listener);
        void loadUser(long userId, OnUserLoadedListener  listener);
        void loadUserPublications(long userId, OnPublicationsLoadedListener listener);
        interface OnUserLoadedListener {
            void onUserLoaded(User user);
            void onUserError(String message);
        }
        interface OnPublicationsLoadedListener {
            void onPublicationsLoaded(List<Publication> publications);
            void onPublicationsError(String message);
        }
        interface OnFavoritesLoadedListener {
            void onFavoritesLoaded(List<FavoritePublication> favorites);
            void onFavoritesError(String message);
        }
    }
}
