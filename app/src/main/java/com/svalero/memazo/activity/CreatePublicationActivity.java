package com.svalero.memazo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.svalero.memazo.R;
import com.svalero.memazo.contract.CreatePublicationContract;
import com.svalero.memazo.databinding.ActivityCreatePublicationBinding;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.presenter.CreatePublicationPresenter;

public class CreatePublicationActivity extends AppCompatActivity implements CreatePublicationContract.View {

    private ActivityCreatePublicationBinding binding;
    private CreatePublicationContract.Presenter presenter;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Si el usuario concede el permiso, reintentamos la acción que lo requería.
                    getCurrentLocationAndPublish();
                } else {
                    // Si el usuario deniega el permiso, informamos y desactivamos la opción.
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

        binding.btnPublish.setOnClickListener(v -> handleCreatePublication());
    }

    /**
     * Configura los spinners con las opciones de los arrays de recursos.
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_content_options, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeContent.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter.createFromResource(this,
                R.array.privacy_options, android.R.layout.simple_spinner_item);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPrivacy.setAdapter(privacyAdapter);
    }

    /**
     * Método principal que se ejecuta al pulsar el botón "Publicar".
     * Valida los datos y decide si obtener la ubicación o publicar directamente.
     */
    private void handleCreatePublication() {
        String content = binding.etPublicationContent.getText().toString().trim();
        if (content.isEmpty()) {
            showError("El contenido es obligatorio.");
            return;
        }

        if (binding.switchLocation.isChecked()) {
            Toast.makeText(this, "Obteniendo ubicación...", Toast.LENGTH_SHORT).show();
            getCurrentLocationAndPublish();
        } else {
            // Si la ubicación no está activada, publicamos directamente sin coordenadas.
            sendPublicationToPresenter(0.0, 0.0);
        }
    }

    /**
     * Intenta obtener la última ubicación conocida del dispositivo.
     * Si no tiene permisos, los solicita.
     * Si la obtiene, publica. Si no, solicita una nueva.
     */
    private void getCurrentLocationAndPublish() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, lo solicitamos. El launcher se encargará del resultado.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Ubicación rápida encontrada. Publicamos con estas coordenadas.
                        Toast.makeText(this, "Ubicación obtenida. Publicando...", Toast.LENGTH_SHORT).show();
                        sendPublicationToPresenter(location.getLatitude(), location.getLongitude());
                    } else {
                        // No hay una "última ubicación", así que pedimos una nueva activamente.
                        requestNewLocationAndPublish();
                    }
                })
                .addOnFailureListener(this, e -> {
                    showError("No se pudo obtener la ubicación: " + e.getMessage());
                    binding.switchLocation.setChecked(false);
                });
    }

    /**
     * Solicita activamente una nueva actualización de la ubicación.
     * Publicará cuando reciba la primera respuesta.
     */
    private void requestNewLocationAndPublish() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Creamos una solicitud de ubicación de alta precisión que se ejecutará una sola vez.
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMaxUpdates(1) // Solo queremos una actualización
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Nueva ubicación recibida. Publicamos con estas coordenadas.
                    Toast.makeText(CreatePublicationActivity.this, "Nueva ubicación obtenida. Publicando...", Toast.LENGTH_SHORT).show();
                    sendPublicationToPresenter(location.getLatitude(), location.getLongitude());
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * Método centralizado que crea el objeto DTO y lo envía al Presenter.
     * @param latitude  La latitud para la publicación.
     * @param longitude La longitud para la publicación.
     */
    private void sendPublicationToPresenter(double latitude, double longitude) {
        String content = binding.etPublicationContent.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String typeContent = binding.spinnerTypeContent.getSelectedItem().toString();
        String privacy = binding.spinnerPrivacy.getSelectedItem().toString();

        // TODO: Obtener el ID de usuario real desde la sesión, SharedPreferences, etc.
        long userId = 1L;

        PublicationInDto publicationDto = new PublicationInDto(
                userId,
                content,
                imageUrl.isEmpty() ? null : imageUrl,
                typeContent,
                privacy,
                latitude,
                longitude
        );

        presenter.createPublication(publicationDto);
    }

    /**
     * Detiene las actualizaciones de ubicación cuando la actividad no está en primer plano
     * para evitar fugas de memoria y consumo de batería.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


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