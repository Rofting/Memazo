package com.svalero.memazo.presenter;

import android.content.Context;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.model.ProfileModel;
import java.util.List;

public class ProfilePresenter implements ProfileContract.Presenter,
        ProfileContract.Model.OnUserLoadedListener,
        ProfileContract.Model.OnPublicationsLoadedListener,
        ProfileContract.Model.OnFavoritesLoadedListener,
        ProfileContract.Model.OnDeletePublicationListener,
        ProfileContract.Model.OnUpdatePublicationListener,
        ProfileContract.Model.OnUserUpdatedListener
{

    private ProfileContract.View view;
    private ProfileContract.Model model;
    private int positionToDelete;

    public ProfilePresenter(ProfileContract.View view, Context context) {
        this.view = view;
        this.model = new ProfileModel(context);
    }

    @Override
    public void loadUserProfile(long userId) {
        model.loadUser(userId, this);
        model.loadUserPublications(userId, this);
    }

    @Override
    public void loadFavorites() {
        model.loadFavorites(this);
    }

    @Override
    public void deletePublication(long publicationId, int position) {
        this.positionToDelete = position;
        model.deletePublication(publicationId, this);
    }

    @Override
    public void updatePublication(long publicationId, PublicationInDto publicationDto) {
        model.updatePublication(publicationId, publicationDto, this);
    }

    @Override
    public void updateUser(long userId, User updatedUser) {
        model.updateUser(userId, updatedUser, this);
    }


    @Override
    public void onUserLoaded(User user) {
        view.showProfileData(user);
    }

    @Override
    public void onUserError(String message) {
        view.showError(message);
    }

    @Override
    public void onPublicationsLoaded(List<Publication> publications) {
        view.showPublicationList(publications);
    }

    @Override
    public void onPublicationsError(String message) {
        view.showError(message);
    }

    @Override
    public void onFavoritesLoaded(List<FavoritePublication> favorites) {
        view.showFavoritePublications(favorites);
    }

    @Override
    public void onFavoritesError(String message) {
        view.showError(message);
    }

    @Override
    public void onDeletePublicationSuccess() {
        view.removePublication(positionToDelete);
    }

    @Override
    public void onDeletePublicationError(String message) {
        view.showError(message);
    }

    @Override
    public void onUpdatePublicationSuccess(Publication publication) {
        view.showUpdateSuccess("Publicación actualizada con éxito");
        view.refreshPublications();
    }

    @Override
    public void onUpdatePublicationError(String message) {
        view.showError(message);
    }

    @Override
    public void onUserUpdated(User user) {
        view.showUpdateSuccess("Perfil actualizado con éxito");
        view.refreshPublications();
    }

    @Override
    public void onUserUpdateError(String message) {
        view.showError(message);
    }
}