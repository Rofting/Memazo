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
        // Le pide al modelo que cargue los datos y se pone a la escucha del resultado
        model.loadPublications(this);
    }

    // Callbacks del Model
    @Override
    public void onSuccess(List<Publication> publications) {
        // Cuando el modelo dice que todo fue bien, le pasa los datos a la vista
        view.showPublications(publications);
    }

    @Override
    public void onError(String message) {
        // Cuando el modelo dice que hubo un error, se lo comunica a la vista
        view.showError(message);
    }
}