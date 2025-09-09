package com.svalero.memazo.contract;

import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;

public interface AuthContract {
    interface View {
        void showRegistrationSuccess();
        void showLoginSuccess(User user);
        void showError(String message);
    }

    interface Presenter {
        void registerUser(User user);
        void loginUser(LoginRequest loginRequest);
    }

    interface Model {
        interface OnRegisterListener {
            void onRegisterSuccess();
            void onRegisterError(String message);
        }
        void registerUser(User user, OnRegisterListener listener);

        interface OnLoginListener {
            void onLoginSuccess(User user);
            void onLoginError(String message);
        }
        void loginUser(LoginRequest loginRequest, OnLoginListener listener);
    }
}