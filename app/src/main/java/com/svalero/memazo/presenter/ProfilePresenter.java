package com.svalero.memazo.presenter;

import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.model.ProfileModel;

import java.util.List;

public class ProfilePresenter implements ProfileContract.Presenter, ProfileContract.Model.OnUserLoadedListener, ProfileContract.Model.OnPublicationsLoadedListener {

    private ProfileContract.View view;
    private ProfileContract.Model model;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        this.model = new ProfileModel();
    }

    @Override
    public void loadUserProfile(long userId) {
        model.loadUser(userId, this);
        model.loadUserPublications(userId, this);
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
}