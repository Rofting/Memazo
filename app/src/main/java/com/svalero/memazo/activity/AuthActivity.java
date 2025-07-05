package com.svalero.memazo.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.memazo.api.ApiClient;
import com.svalero.memazo.api.UserApiInterface;
import com.svalero.memazo.databinding.ActivityAuthBinding;
import com.svalero.memazo.domain.User;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    // Declara la variable para el View Binding
    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Infla el layout y asigna la instancia de binding
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura el listener para el botón de registro
        binding.btnRegister.setOnClickListener(v -> validateAndRegister());

        // Configura el selector de fecha
        setupDatePicker();
    }

    /**
     * Valida los datos del formulario y, si son correctos, inicia el registro vía API.
     */
    private void validateAndRegister() {
        // 1. Recoger datos de la interfaz
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String displayDate = binding.etBirthDate.getText().toString().trim();

        // 2. Validación de campos obligatorios
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || displayDate.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Recoger datos restantes
        String avatar = binding.etAvatar.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String birthDateForApi = (String) binding.etBirthDate.getTag();

        int selectedSexId = binding.rgSex.getCheckedRadioButtonId();
        if (selectedSexId == -1) {
            Toast.makeText(this, "Por favor, selecciona tu sexo", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex = binding.rbMale.isChecked() ? "Male" : "Female";

        // 4. Crear el objeto de usuario para enviar a la API
        User user = new User(name, email, password, birthDateForApi, avatar, phone, sex, true);

        // 5. Preparar y ejecutar la llamada a la API
        UserApiInterface api = ApiClient.getRetrofitService(UserApiInterface.class);
        Call<User> call = api.addUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Se ejecuta cuando el servidor responde
                if (response.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AuthActivity.this, "Error en el registro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Si hay un fallo en la conexion
                Toast.makeText(AuthActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDatePicker() {
        binding.etBirthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formato para mostrar al usuario (ej: 5/7/2025)
                        String displayFormat = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.etBirthDate.setText(displayFormat);

                        // Formato para enviar a la API (ej: 2025-07-05)
                        String apiFormat = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                        binding.etBirthDate.setTag(apiFormat);

                    }, year, month, day);

            // Evita que se puedan seleccionar fechas futuras
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Evita que se abra el teclado al pulsar el campo
        binding.etBirthDate.setFocusable(false);
    }
}