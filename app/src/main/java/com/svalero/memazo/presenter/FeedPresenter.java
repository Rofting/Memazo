package com.svalero.memazo.presenter;

import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.model.FeedModel;

import java.util.List;

public class FeedPresenter implements FeedContract.Presenter, FeedContract.Model.OnLoadPublicationsListener {

    private FeedContract.View view;
    private FeedContract.Model model;

    public FeedPresenter(FeedContract.View view) {
        this.view = view;
        this.model = new FeedModel();
    }

    @Override
    public void loadPublications() {
        model.loadPublications(this);
    }

    @Override
    public void onSuccess(List<Publication> publications) {
        view.showPublications(publications);
    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }
}