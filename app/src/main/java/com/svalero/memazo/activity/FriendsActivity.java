package com.svalero.memazo.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.memazo.R;
import com.svalero.memazo.adapter.FriendsAdapter;
import com.svalero.memazo.contract.FriendsContract;
import com.svalero.memazo.domain.FriendshipOutDto;
import com.svalero.memazo.presenter.FriendsPresenter;

import java.util.List;

public class FriendsActivity extends AppCompatActivity implements FriendsContract.View {

    private RecyclerView rvFriends;
    private FriendsAdapter friendsAdapter;
    private FriendsContract.Presenter presenter;
    private long currentUserId = -1L;

    private int pendingDeletePos = -1;
    private long pendingDeleteId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setSupportActionBar(findViewById(R.id.toolbar_friends));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        currentUserId = sp.getLong("logged_in_user_id", -1L);

        rvFriends = findViewById(R.id.rv_friends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));

        presenter = new FriendsPresenter(this);

        friendsAdapter = new FriendsAdapter(this, currentUserId, (friendship, position) -> {
            String name = (friendship.getFriend() != null && friendship.getFriend().getName() != null)
                    ? friendship.getFriend().getName()
                    : (friendship.getFriendName() != null ? friendship.getFriendName() : "este usuario");
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar amistad")
                    .setMessage("¿Seguro que quieres eliminar a " + name + " de tus amistades?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        pendingDeletePos = position;
                        pendingDeleteId = friendship.getId();
                        presenter.deleteFriendship(friendship.getId());
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        rvFriends.setAdapter(friendsAdapter);

        load();
    }

    private void load() {
        if (currentUserId > 0) {
            presenter.loadFriends(currentUserId);
        } else {
            showError("Usuario no identificado");
        }
    }

    // ===== Implementación de la View del contrato =====

    @Override
    public void showFriendships(List<FriendshipOutDto> friendships) {
        friendsAdapter.setFriendships(friendships);
    }

    public void showFriends(List<FriendshipOutDto> friendships) {
        friendsAdapter.setFriendships(friendships);
    }

    @Override
    public void onFriendDeleted(long friendshipId) {
        if (pendingDeletePos >= 0) {
            friendsAdapter.removeAt(pendingDeletePos);
            pendingDeletePos = -1;
        }
        Toast.makeText(this, "Amistad eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFriendDeleteError(long friendshipId, String message) {
        Toast.makeText(this, "No se pudo eliminar la amistad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message != null ? message : "Error desconocido", Toast.LENGTH_LONG).show();
    }
}

