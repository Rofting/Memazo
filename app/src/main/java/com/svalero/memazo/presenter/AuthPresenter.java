package com.svalero.memazo.presenter;

import com.svalero.memazo.contract.AuthContract;
import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.model.AuthModel;

public class AuthPresenter implements AuthContract.Presenter, AuthContract.Model.OnRegisterListener, AuthContract.Model.OnLoginListener {

    private AuthContract.View view;
    private AuthContract.Model model;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
        this.model = new AuthModel();
    }

    @Override
    public void registerUser(User user) {
        model.registerUser(user, this);
    }

    @Override
    public void loginUser(LoginRequest loginRequest) {
        model.loginUser(loginRequest, this);
    }

    @Override
    public void onRegisterSuccess() {
        view.showRegistrationSuccess();
    }

    @Override
    public void onRegisterError(String message) {
        view.showError(message);
    }

    @Override
    public void onLoginSuccess(User user) {
        view.showLoginSuccess(user);
    }

    @Override
    public void onLoginError(String message) {
        view.showError(message);
    }
}