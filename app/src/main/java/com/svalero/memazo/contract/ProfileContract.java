package com.svalero.memazo.contract;

import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.domain.User;
import java.util.List;

public interface ProfileContract {

    interface View {
        void showProfileData(User user);
        void showPublicationList(List<Publication> publications);
        void showFavoritePublications(List<FavoritePublication> favorites);
        void removePublication(int position);
        void refreshPublications();
        void showError(String message);
        void showUpdateSuccess(String message);
    }



    interface Presenter {
        void loadUserProfile(long userId);
        void loadFavorites();
        void deletePublication(long publicationId, int position);
        void updatePublication(long publicationId, PublicationInDto publicationDto);
        void updateUser(long userId, User updatedUser);
    }


    interface Model {
        interface OnUserUpdatedListener {
            void onUserUpdated(User user);
            void onUserUpdateError(String message);
        }
        void updateUser(long userId, User updatedUser, OnUserUpdatedListener listener);

        interface OnUserLoadedListener {
            void onUserLoaded(User user);
            void onUserError(String message);
        }
        void loadUser(long userId, OnUserLoadedListener listener);

        interface OnPublicationsLoadedListener {
            void onPublicationsLoaded(List<Publication> publications);
            void onPublicationsError(String message);
        }
        void loadUserPublications(long userId, OnPublicationsLoadedListener listener);

        interface OnFavoritesLoadedListener {
            void onFavoritesLoaded(List<FavoritePublication> favorites);
            void onFavoritesError(String message);
        }
        void loadFavorites(OnFavoritesLoadedListener listener);

        interface OnDeletePublicationListener {
            void onDeletePublicationSuccess();
            void onDeletePublicationError(String message);
        }
        void deletePublication(long publicationId, OnDeletePublicationListener listener);

        interface OnUpdatePublicationListener {
            void onUpdatePublicationSuccess(Publication publication);
            void onUpdatePublicationError(String message);
        }
        void updatePublication(long publicationId, PublicationInDto publicationDto, OnUpdatePublicationListener listener);
    }
}