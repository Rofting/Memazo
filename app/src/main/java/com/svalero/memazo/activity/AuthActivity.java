package com.svalero.memazo.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.svalero.memazo.contract.AuthContract;
import com.svalero.memazo.databinding.ActivityAuthBinding;
import com.svalero.memazo.domain.LoginRequest;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.presenter.AuthPresenter;
import java.util.Calendar;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private ActivityAuthBinding binding;
    private AuthContract.Presenter presenter;
    private boolean isLoginMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // La Vista crea su Presenter, pasándose a sí misma como la Vista
        presenter = new AuthPresenter(this);

        // Los listeners ahora llaman a los métodos de la Activity, que a su vez llamarán al Presenter
        binding.btnRegister.setOnClickListener(v -> validateAndRegister());
        binding.btnLogin.setOnClickListener(v -> validateAndLogin());
        binding.tvToggleMode.setOnClickListener(v -> toggleMode());

        setupDatePicker();
        // Inicializamos la vista en modo registro por defecto
        toggleMode();
        toggleMode();
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            binding.tilName.setVisibility(View.GONE);
            binding.tilBirthDate.setVisibility(View.GONE);
            binding.tilAvatar.setVisibility(View.GONE);
            binding.tilPhone.setVisibility(View.GONE);
            binding.tvSexLabel.setVisibility(View.GONE);
            binding.rgSex.setVisibility(View.GONE);
            binding.btnRegister.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.tvTitle.setText("Iniciar Sesión");
            binding.tvToggleMode.setText("¿No tienes cuenta? Regístrate");
        } else {
            binding.tilName.setVisibility(View.VISIBLE);
            binding.tilBirthDate.setVisibility(View.VISIBLE);
            binding.tilAvatar.setVisibility(View.VISIBLE);
            binding.tilPhone.setVisibility(View.VISIBLE);
            binding.tvSexLabel.setVisibility(View.VISIBLE);
            binding.rgSex.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
            binding.tvTitle.setText("Crear Cuenta");
            binding.tvToggleMode.setText("¿Ya tienes cuenta? Inicia Sesión");
        }
    }

    private void validateAndRegister() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String displayDate = binding.etBirthDate.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || displayDate.isEmpty()) {
            showError("Por favor, rellena todos los campos obligatorios");
            return;
        }

        String birthDateForApi = (String) binding.etBirthDate.getTag();
        String avatar = binding.etAvatar.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String sex = binding.rbMale.isChecked() ? "Male" : "Female";

        User user = new User(name, email, password, birthDateForApi, avatar, phone, sex, true);

        // La Vista le pasa los datos al Presenter y se olvida del tema
        presenter.registerUser(user);
    }

    private void validateAndLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Email y contraseña son obligatorios");
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        // La Vista le pasa los datos al Presenter
        presenter.loginUser(loginRequest);
    }

    private void setupDatePicker() {
        binding.etBirthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String displayFormat = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.etBirthDate.setText(displayFormat);
                        String apiFormat = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                        binding.etBirthDate.setTag(apiFormat);
                    }, year, month, day);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
        binding.etBirthDate.setFocusable(false);
    }

    private void navigateToFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    // MÉTODOS DE LA VISTA (LLAMADOS POR EL PRESENTER)

    @Override
    public void showRegistrationSuccess() {
        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoginSuccess() {
        Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
        navigateToFeed();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}