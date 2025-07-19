package com.svalero.memazo.contract;

import com.svalero.memazo.domain.Publication;

import java.util.List;

public interface MapContract {
    interface View {
        void showPublicationsOnMap(List<Publication> publications);
        void showError(String message);
    }

    interface Presenter {
        void loadPublications();
    }

}
