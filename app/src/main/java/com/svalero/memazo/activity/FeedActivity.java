package com.svalero.memazo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        // La vista crea su Presenter
        presenter = new FeedPresenter(this);

        // Inicializamos el Adapter con una lista vacía
        publicationAdapter = new PublicationAdapter(new ArrayList<>());

        // Configuramos el RecyclerView
        binding.rvPublications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPublications.setAdapter(publicationAdapter);

        // Le decimos al Presenter que empiece a cargar las publicaciones
        presenter.loadPublications();

        binding.fabAddPublication.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePublicationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showPublications(List<Publication> publications) {
        // El adapter recibe la nueva lista y notifica al RecyclerView
        publicationAdapter.setPublications(publications);
        publicationAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}