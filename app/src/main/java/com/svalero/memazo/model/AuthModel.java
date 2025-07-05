package com.svalero.memazo.model;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.UserApiInterface;
import com.svalero.memazo.contract.AuthContract;
import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthModel implements AuthContract.Model {
    @Override
    public void registerUser(User user, OnRegisterListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
        Call<User> call = api.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    listener.onRegisterSuccess();
                } else {
                    listener.onRegisterError("Error en el registro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onRegisterError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    @Override
    public void loginUser(LoginRequest loginRequest, OnLoginListener listener) {
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);

        Call<User> call = api.login(loginRequest);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    listener.onLoginSuccess();
                } else {
                    listener.onLoginError("Email o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onLoginError("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}