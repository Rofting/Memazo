package com.svalero.memazo.presenter;

import com.svalero.memazo.contract.AuthContract;
import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.model.AuthModel;

// El Presenter debe implementar los dos listeners del Model (para registro y para login)
public class AuthPresenter implements AuthContract.Presenter, AuthContract.Model.OnRegisterListener, AuthContract.Model.OnLoginListener {

    private final AuthContract.View view;
    private final AuthContract.Model model;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
        this.model = new AuthModel(); // El Presenter crea su Model
    }

    /**
     * Pide al Model que inicie el registro de un usuario.
     */
    @Override
    public void registerUser(User user) {
        model.registerUser(user, this);
    }

    /**
     * Pide al Model que inicie el login de un usuario.
     */
    @Override
    public void loginUser(LoginRequest loginRequest) {
        model.loginUser(loginRequest, this);
    }

    // Callbacks del Model para el REGISTRO
    @Override
    public void onRegisterSuccess() {
        view.showRegistrationSuccess();
    }

    @Override
    public void onRegisterError(String message) {
        view.showError(message);
    }

    // Callbacks del Model para el LOGIN
    @Override
    public void onLoginSuccess() {
        view.showLoginSuccess();
    }

    @Override
    public void onLoginError(String message) {
        view.showError(message);
    }
}