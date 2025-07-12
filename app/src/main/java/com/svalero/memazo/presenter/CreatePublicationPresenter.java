package com.svalero.memazo.presenter;


import com.svalero.memazo.contract.CreatePublicationContract;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.model.CreatePublicationModel;

public class CreatePublicationPresenter implements CreatePublicationContract.Presenter, CreatePublicationContract.Model.OnCreatePublicationListener {
    private CreatePublicationContract.View view;
    private CreatePublicationContract.Model model;

    public CreatePublicationPresenter(CreatePublicationContract.View view) {
        this.view = view;
        this.model = new CreatePublicationModel();
    }

    @Override
    public void createPublication(PublicationInDto publication) {
        model.createPublication(publication, this);
    }

    @Override
    public void onSuccess() {
        view.showSuccess("Publicación creada con éxito");
        view.finishView();
    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }
}