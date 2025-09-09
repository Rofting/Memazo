package com.svalero.memazo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.memazo.R;
import com.svalero.memazo.databinding.ActivitySettingsBinding;


public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_entries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSettingsTheme.setAdapter(adapter);

        binding.btnSaveSettings.setOnClickListener(v -> savePreferences());
        loadPreferences();
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        String theme = sharedPreferences.getString("theme", "default");
        String username = sharedPreferences.getString("username", "");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_entries, android.R.layout.simple_spinner_item);
        int position = adapter.getPosition(theme);
        binding.spinnerSettingsTheme.setSelection(position);


        binding.switchSettingsNotifications.setChecked(notificationsEnabled);
        binding.etSettingsUsername.setText(username);
    }
    private void savePreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("notifications", binding.switchSettingsNotifications.isChecked());


        editor.putString("theme", binding.spinnerSettingsTheme.getSelectedItem().toString());

        editor.putString("username", binding.etSettingsUsername.getText().toString());

        editor.apply();

        Toast.makeText(this, getString(R.string.settings_saved_ok), Toast.LENGTH_SHORT).show();

    }
}
