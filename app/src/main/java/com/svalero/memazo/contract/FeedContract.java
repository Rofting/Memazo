package com.svalero.memazo.contract;

import com.svalero.memazo.domain.Publication;
import java.util.List;

public interface FeedContract {

    /**
     * Define los métodos que la Vista (FeedActivity) debe implementar
     * para que el Presenter pueda comunicarse con ella.
     */
    interface View {
        void showPublications(List<Publication> publications);
        void showError(String message);
    }

    /**
     * Define los métodos que el Presenter (FeedPresenter) debe implementar
     * para que la Vista pueda comunicarse con él.
     */
    interface Presenter {
        void loadPublications();
    }

    /**
     * Define los métodos que el Model (FeedModel) debe implementar
     * para que el Presenter pueda pedirle datos.
     */
    interface Model {
        // Interfaz para que el Model notifique al Presenter el resultado
        interface OnLoadPublicationsListener {
            void onSuccess(List<Publication> publications);
            void onError(String message);
        }

        void loadPublications(OnLoadPublicationsListener listener);
    }
}