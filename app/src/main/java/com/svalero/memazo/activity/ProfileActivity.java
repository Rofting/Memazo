package com.svalero.memazo.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.adapter.PublicationAdapter;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.databinding.ActivityProfileBinding;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.presenter.ProfilePresenter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {

    private ProfileContract.Presenter presenter;
    private ActivityProfileBinding binding;
    private PublicationAdapter publicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        presenter = new ProfilePresenter(this);
        publicationAdapter = new PublicationAdapter(this, new ArrayList<>());

        binding.rvProfilePublications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProfilePublications.setAdapter(publicationAdapter);

        presenter.loadUserProfile(1L);
    }

    @Override
    public void showProfileData(User user) {
        binding.tvProfileUsername.setText(user.getName());
        Glide.with(this)
                .load(user.getAvatar())
                .into(binding.ivProfileAvatar);
    }

    @Override
    public void showPublicationList(List<Publication> publications) {
        publicationAdapter.setPublications(publications);
        publicationAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}