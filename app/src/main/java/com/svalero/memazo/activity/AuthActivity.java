package com.svalero.memazo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.memazo.R;
import com.svalero.memazo.contract.AuthContract;
import com.svalero.memazo.databinding.ActivityAuthBinding;
import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.presenter.AuthPresenter;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private ActivityAuthBinding binding;
    private AuthContract.Presenter presenter;

    private boolean isLoginMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new AuthPresenter(this);

        binding.tvToggleMode.setOnClickListener(v -> toggleMode());

        binding.btnRegister.setOnClickListener(v -> validateAndRegister());

        binding.btnLogin.setOnClickListener(v -> validateAndLogin());

        // Estado inicial: Registro
        isLoginMode = false;
        applyModeUI();
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        applyModeUI();
    }

    private void applyModeUI() {
        if (isLoginMode) {
            // MODO LOGIN
            binding.tvTitle.setText(getString(R.string.login));
            binding.tvToggleMode.setText(getString(R.string.toggle_to_register));

            binding.tilName.setVisibility(View.GONE);
            binding.tilBirthDate.setVisibility(View.GONE);
            binding.tilAvatar.setVisibility(View.GONE);
            binding.tilPhone.setVisibility(View.GONE);
            binding.tvSexLabel.setVisibility(View.GONE);
            binding.rgSex.setVisibility(View.GONE);

            binding.btnRegister.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
        } else {
            // MODO REGISTRO
            binding.tvTitle.setText(getString(R.string.create_account));
            binding.tvToggleMode.setText(getString(R.string.toggle_to_login));

            binding.tilName.setVisibility(View.VISIBLE);
            binding.tilBirthDate.setVisibility(View.VISIBLE);
            binding.tilAvatar.setVisibility(View.VISIBLE);
            binding.tilPhone.setVisibility(View.VISIBLE);
            binding.tvSexLabel.setVisibility(View.VISIBLE);
            binding.rgSex.setVisibility(View.VISIBLE);

            binding.btnRegister.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
        }
    }

    // Registro

    private void validateAndRegister() {
        String name = safeText(binding.etName);
        String email = safeText(binding.etEmail);
        String password = safeText(binding.etPassword);
        String birthDate = safeText(binding.etBirthDate);

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
            showError(getString(R.string.error_fill_all_fields));
            return;
        }

        String avatar = safeText(binding.etAvatar);
        String phone = safeText(binding.etPhone);
        String sex = binding.rbMale.isChecked() ? getString(R.string.male) : getString(R.string.female);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setBirthDate(birthDate);
        user.setAvatar(avatar);
        user.setPhone(phone);
        user.setSex(sex);
        user.setActive(true);

        presenter.registerUser(user);
    }

    private void validateAndLogin() {
        String email = safeText(binding.etEmail);
        String password = safeText(binding.etPassword);

        if (email.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.error_email_password_required));
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);
        presenter.loginUser(loginRequest);
    }


    @Override
    public void showRegistrationSuccess() {
        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
        if (!isLoginMode) {
            // después de registrarse, cambia a login
            isLoginMode = true;
            applyModeUI();
        }
    }

    public void showRegistrationError(String message) {
        showError(message != null ? message : "Registration error");
    }

    @Override
    public void showLoginSuccess(User user) {
        if (user != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            sp.edit()
                    .putLong("logged_in_user_id", user.getId())
                    .putString("logged_in_user_name", user.getName())
                    .apply();
        }
        Toast.makeText(this,
                getString(R.string.login_success_welcome, user != null ? user.getName() : ""),
                Toast.LENGTH_SHORT).show();

        // Navega al Feed
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void showLoginError(String message) {
        showError(message != null ? message : "Login error");
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private static String safeText(android.widget.EditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
