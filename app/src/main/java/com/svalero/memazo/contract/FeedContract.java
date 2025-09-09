package com.svalero.memazo.contract;

import com.svalero.memazo.domain.Publication;
import java.util.List;

public interface FeedContract {

    interface View {
        void showPublications(List<Publication> publications);
        void removePublication(int position);
        void showError(String message);
    }

    interface Presenter {
        void loadPublications();
        void deletePublication(long publicationId, int position);
    }

    interface Model {
        // Listener para el resultado de cargar publicaciones
        interface OnLoadPublicationsListener {
            void onLoadPublicationsSuccess(List<Publication> publications);
            void onLoadPublicationsError(String message);
        }

        // Listener para el resultado de borrar una publicación
        interface OnDeletePublicationListener {
            void onDeleteSuccess();
            void onDeleteError(String message);
        }

        void loadPublications(OnLoadPublicationsListener listener);
        void deletePublication(long publicationId, OnDeletePublicationListener listener);
    }
}