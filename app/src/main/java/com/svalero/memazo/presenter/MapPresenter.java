package com.svalero.memazo.presenter;

import android.content.Context;

import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.contract.MapContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.model.FeedModel;

import java.util.List;

public class MapPresenter implements MapContract.Presenter, FeedContract.Model.OnLoadPublicationsListener {
    private MapContract.View view;
    private FeedModel model;

    public MapPresenter(MapContract.View view, Context context) {
        this.view = view;
        this.model = new FeedModel(context);
    }

    @Override
    public void loadPublications() {
        model.loadPublications(this);
        }

    @Override
    public void onLoadPublicationsSuccess(List<Publication> publications) {
        view.showPublicationsOnMap(publications);
    }

    @Override
    public void onLoadPublicationsError(String message) {
        view.showError(message);
    }
}
