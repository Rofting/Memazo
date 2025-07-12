package com.svalero.memazo.contract;

import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;

public interface CreatePublicationContract {
    interface View {
        void showSuccess(String message);
        void showError(String message);
        void finishView(); // Para cerrar la activity tras publicar
    }

    interface Presenter {
        void createPublication(PublicationInDto publication);
    }

    interface Model {
        interface OnCreatePublicationListener {
            void onSuccess();
            void onError(String message);
        }
        void createPublication(PublicationInDto publication, OnCreatePublicationListener listener);
    }
}