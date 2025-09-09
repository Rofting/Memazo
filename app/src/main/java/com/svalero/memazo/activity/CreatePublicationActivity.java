package com.svalero.memazo.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
    private double latitude = 0.0;
    private double longitude = 0.0;
    private PublicationInDto pendingPublicationDto;

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

        binding.btnPublish.setOnClickListener(v -> createPublication());
    }

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

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
                Toast.makeText(this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();
                if (pendingPublicationDto != null) {
                    sendPublicationToPresenter();
                }
            } else {
                requestNewLocation();
            }
        });
    }

    private void requestNewLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(CreatePublicationActivity.this, "Nueva ubicación obtenida", Toast.LENGTH_SHORT).show();
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    if (pendingPublicationDto != null) {
                        sendPublicationToPresenter();
                    }
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void createPublication() {
        String content = binding.etPublicationContent.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String typeContent = binding.spinnerTypeContent.getSelectedItem().toString();
        String privacy = binding.spinnerPrivacy.getSelectedItem().toString();

        if (content.isEmpty()) {
            showError(getString(R.string.error_content_required));
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long userId = sharedPreferences.getLong("logged_in_user_id", -1L);

        if (userId == -1L) {
            showError("Error: No se ha encontrado un usuario válido.");
            return;
        }

        pendingPublicationDto = new PublicationInDto(userId, content, imageUrl.isEmpty() ? null : imageUrl, typeContent, privacy, 0.0, 0.0);

        if (binding.switchLocation.isChecked()) {
            requestLocationPermission();
        } else {
            sendPublicationToPresenter();
        }
    }

    private void sendPublicationToPresenter() {
        if (pendingPublicationDto == null) return;

        if (binding.switchLocation.isChecked()) {
            pendingPublicationDto.setLatitude(this.latitude);
            pendingPublicationDto.setLongitude(this.longitude);
        }

        presenter.createPublication(pendingPublicationDto);
        pendingPublicationDto = null;
    }

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
        finish();
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