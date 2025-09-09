package com.svalero.memazo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.adapter.FavoritePublicationAdapter;
import com.svalero.memazo.adapter.PublicationAdapter;
import com.svalero.memazo.contract.ProfileContract;
import com.svalero.memazo.databinding.ActivityProfileBinding;
import com.svalero.memazo.dialog.EditProfileDialogFragment;
import com.svalero.memazo.dialog.EditPublicationDialogFragment;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.domain.PublicationInDto;
import com.svalero.memazo.domain.User;
import com.svalero.memazo.presenter.ProfilePresenter;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View,
        PublicationAdapter.OnPublicationListener,
        EditPublicationDialogFragment.EditPublicationListener,
        EditProfileDialogFragment.EditProfileListener {

    private ProfileContract.Presenter presenter;
    private ActivityProfileBinding binding;
    private PublicationAdapter publicationAdapter;
    private FavoritePublicationAdapter favoriteAdapter;
    private long currentUserId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentUserId = sharedPreferences.getLong("logged_in_user_id", -1L);

        if (currentUserId == -1L) {
            showError("No se ha iniciado sesión.");
            finish();
            return;
        }

        presenter = new ProfilePresenter(this, this);

        binding.btnEditProfile.setOnClickListener(v -> {
            if (currentUser != null) {
                EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance(currentUser);
                dialog.show(getSupportFragmentManager(), "edit_profile_dialog");
            } else {
                showError("Datos del usuario no cargados. Inténtalo de nuevo.");
            }
        });

        publicationAdapter = new PublicationAdapter(this, new ArrayList<>(), this, true);
        binding.rvProfilePublications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProfilePublications.setAdapter(publicationAdapter);

        favoriteAdapter = new FavoritePublicationAdapter(this, new ArrayList<>());
        binding.rvFavoritePublications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFavoritePublications.setAdapter(favoriteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPublications();
    }

    @Override
    public void showProfileData(User user) {
        this.currentUser = user;
        binding.tvProfileUsername.setText(user.getName());
        Glide.with(this).load(user.getAvatar()).error(R.drawable.ic_profile).into(binding.ivProfileAvatar);
    }

    @Override
    public void showPublicationList(List<Publication> publications) {
        publicationAdapter.setPublications(publications);
        publicationAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFavoritePublications(List<FavoritePublication> favorites) {
        favoriteAdapter.setFavorites(favorites);
        favoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void removePublication(int position) {
        publicationAdapter.removePublicationAt(position);
        Toast.makeText(this, "Publicación eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshPublications() {
        presenter.loadUserProfile(currentUserId);
        presenter.loadFavorites();
    }

    @Override
    public void showUpdateSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClicked(long publicationId, int position) {
        presenter.deletePublication(publicationId, position);
    }

    @Override
    public void onEditClicked(Publication publication) {
        EditPublicationDialogFragment dialog = EditPublicationDialogFragment.newInstance(publication);
        dialog.show(getSupportFragmentManager(), "edit_dialog");
    }

    @Override
    public void onPublicationEdited(long publicationId, PublicationInDto publicationDto) {
        presenter.updatePublication(publicationId, publicationDto);
    }

    @Override
    public void onProfileEdited(User updatedUser) {
        presenter.updateUser(updatedUser.getId(), updatedUser);
    }
}