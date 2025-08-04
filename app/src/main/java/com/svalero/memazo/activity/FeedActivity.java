package com.svalero.memazo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.svalero.memazo.R;
import com.svalero.memazo.adapter.PublicationAdapter;
import com.svalero.memazo.contract.FeedContract;
import com.svalero.memazo.databinding.ActivityFeedBinding;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.presenter.FeedPresenter;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements FeedContract.View {

    private ActivityFeedBinding binding;
    private FeedContract.Presenter presenter;
    private PublicationAdapter publicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbarFeed);

        getSupportActionBar().setTitle("Memazo");

        presenter = new FeedPresenter(this);

        publicationAdapter = new PublicationAdapter(this, new ArrayList<>());
        binding.rvPublications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPublications.setAdapter(publicationAdapter);

        binding.fabAddPublication.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePublicationActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadPublications();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadPublications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_view_map) {
            // Si se pulsa el ítem del mapa, abrimos la MapActivity
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.action_profile) {
            // Si se pulsa el ítem del perfil, abrimos la ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPublications(List<Publication> publications) {
        publicationAdapter.setPublications(publications);
        publicationAdapter.notifyDataSetChanged();
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        binding.swipeRefreshLayout.setRefreshing(false);
    }
}