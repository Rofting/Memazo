package com.svalero.memazo.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.db.AppDatabase;
import com.svalero.memazo.db.FavoritePublicationDao;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    public interface OnPublicationListener {
        void onDeleteClicked(long publicationId, int position);
        void onEditClicked(Publication publication);
    }

    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final OnPublicationListener listener;
    private final boolean showAdminButtons;
    private List<Publication> publications = new ArrayList<>();

    public PublicationAdapter(Context context,
                              List<Publication> publications,
                              OnPublicationListener listener,
                              boolean showAdminButtons) {
        this.context = context;
        this.publications = publications != null ? publications : new ArrayList<>();
        this.listener = listener;
        this.showAdminButtons = showAdminButtons;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications != null ? publications : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removePublicationAt(int position) {
        if (position >= 0 && position < publications.size()) {
            publications.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        Publication publication = publications.get(position);

        holder.tvAuthor.setText(publication.getUserName());
        holder.tvDescription.setText(publication.getContent());

        Glide.with(holder.itemView.getContext())
                .load(publication.getImageUrl())
                .fallback(R.drawable.ic_image_not_found)
                .error(R.drawable.ic_image_not_found)
                .into(holder.ivImage);

        if (showAdminButtons) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.action_delete)
                        .setMessage(R.string.delete_friend_message)
                        .setPositiveButton(R.string.action_delete, (d, w) -> {
                            if (listener != null) {
                                int adapterPos = holder.getBindingAdapterPosition();
                                listener.onDeleteClicked(publication.getId(), adapterPos);
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, null)
                        .show();
            });

            holder.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClicked(publication);
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
        }

        holder.cbFavorite.setOnCheckedChangeListener(null);
        holder.cbFavorite.setChecked(false);
        holder.cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                FavoritePublicationDao dao = db.favoritePublicationDao();

                if (isChecked) {
                    FavoritePublication fav = new FavoritePublication();
                    fav.setUserName(publication.getUserName());
                    fav.setContent(publication.getContent());
                    fav.setImageUrl(publication.getImageUrl());
                    dao.insert(fav);
                } else {
                    FavoritePublication fake = new FavoritePublication();
                    fake.setId(publication.getId());
                    dao.delete(fake);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return publications != null ? publications.size() : 0;
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAuthor;
        final TextView tvDescription;
        final ImageView ivImage;
        final CheckBox cbFavorite;
        final ImageButton btnDelete, btnEdit;

        PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvPublicationAuthor);
            tvDescription = itemView.findViewById(R.id.tvPublicationDescription);
            ivImage = itemView.findViewById(R.id.ivPublicationImage);
            cbFavorite = itemView.findViewById(R.id.cb_favorite);
            btnDelete = itemView.findViewById(R.id.btn_delete_publication);
            btnEdit = itemView.findViewById(R.id.btn_edit_publication);
        }
    }
}
