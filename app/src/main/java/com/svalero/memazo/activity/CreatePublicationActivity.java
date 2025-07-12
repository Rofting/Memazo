package com.svalero.memazo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.svalero.memazo.R;
import com.svalero.memazo.contract.CreatePublicationContract;
import com.svalero.memazo.databinding.ActivityCreatePublicationBinding;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.presenter.CreatePublicationPresenter;

public class CreatePublicationActivity extends AppCompatActivity implements CreatePublicationContract.View {

    private ActivityCreatePublicationBinding binding;
    private CreatePublicationContract.Presenter presenter;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;

    // Launcher para el permiso de ubicación
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
                    binding.switchLocation.setChecked(false);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CreatePublicationPresenter(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupSpinners();

        binding.switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                requestLocationPermission();
            }
        });

        binding.btnPublish.setOnClickListener(v -> {
            createPublication();
        });
    }

    private void setupSpinners() {
        // Configurar Spinner para TypeContent
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_content_options, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeContent.setAdapter(typeAdapter);

        // Configurar Spinner para Privacy
        ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter.createFromResource(this,
                R.array.privacy_options, android.R.layout.simple_spinner_item);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPrivacy.setAdapter(privacyAdapter);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                            Toast.makeText(this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void createPublication() {
        String content = binding.etPublicationContent.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String typeContent = binding.spinnerTypeContent.getSelectedItem().toString();
        String privacy = binding.spinnerPrivacy.getSelectedItem().toString();

        if (content.isEmpty()) {
            showError("El contenido es obligatorio.");
            return;
        }

        // Usaremos un ID de usuario de prueba
        long userId = 1L;

        // Si el switch no está activado, enviamos lat/lon como 0.0
        double lat = binding.switchLocation.isChecked() ? this.latitude : 0.0;
        double lon = binding.switchLocation.isChecked() ? this.longitude : 0.0;

        PublicationInDto publicationDto = new PublicationInDto(userId, content, imageUrl.isEmpty() ? null : imageUrl, typeContent, privacy, lat, lon);
        presenter.createPublication(publicationDto);
    }

    // Implementación de los métodos de la vista
    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void finishView() {
        finish();
    }
}