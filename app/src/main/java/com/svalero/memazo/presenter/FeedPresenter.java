package com.svalero.memazo.presenter;

import android.content.Context;
import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.model.FeedModel;
import java.util.List;

public class FeedPresenter implements FeedContract.Presenter, FeedContract.Model.OnLoadPublicationsListener, FeedContract.Model.OnDeletePublicationListener {

    private FeedContract.View view;
    private FeedContract.Model model;
    private int positionToDelete;

    public FeedPresenter(FeedContract.View view, Context context) {
        this.view = view;
        this.model = new FeedModel(context);
    }

    @Override
    public void loadPublications() {
        model.loadPublications(this);
    }

    @Override
    public void deletePublication(long publicationId, int position) {
        this.positionToDelete = position;
        model.deletePublication(publicationId, this);
    }


    @Override
    public void onLoadPublicationsSuccess(List<Publication> publications) {
        view.showPublications(publications);
    }

    @Override
    public void onLoadPublicationsError(String message) {
        view.showError(message);
    }

    @Override
    public void onDeleteSuccess() {
        view.removePublication(positionToDelete);
    }

    @Override
    public void onDeleteError(String message) {
        view.showError(message);
    }
}