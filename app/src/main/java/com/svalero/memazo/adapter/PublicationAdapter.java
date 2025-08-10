package com.svalero.memazo.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.db.AppDatabase;
import com.svalero.memazo.domain.FavoritePublication;
import com.svalero.memazo.domain.Publication;
import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    private Context context;
    private List<Publication> publications;

    public PublicationAdapter(Context context, List<Publication> publications) {
        this.context = context;
        this.publications = publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publication, parent, false);
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

        // Comprobar el estado inicial en segundo plano
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            FavoritePublication favorite = db.favoritePublicationDao().getById(publication.getId());

            // Volvemos al hilo principal para actualizar la interfaz
            new Handler(Looper.getMainLooper()).post(() -> {
                // Desactivamos el listener temporalmente para evitar que se dispare solo
                holder.cbFavorite.setOnCheckedChangeListener(null);
                holder.cbFavorite.setChecked(favorite != null);
                // Lo volvemos a activar con la lógica de guardar/borrar
                setFavoriteChangeListener(holder, publication);
            });
        });
    }

    /**
     * Configura el listener del CheckBox para guardar o borrar un favorito.
     */
    private void setFavoriteChangeListener(PublicationViewHolder holder, Publication publication) {
        holder.cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Tarea 2: Guardar o borrar en segundo plano
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                if (isChecked) {
                    // Si se marca, lo guardamos
                    FavoritePublication newFavorite = new FavoritePublication();
                    newFavorite.setId(publication.getId());
                    newFavorite.setUserName(publication.getUserName());
                    newFavorite.setContent(publication.getContent());
                    newFavorite.setImageUrl(publication.getImageUrl());
                    db.favoritePublicationDao().insert(newFavorite);
                } else {
                    // Si se desmarca, lo borramos
                    FavoritePublication favoriteToDelete = new FavoritePublication();
                    favoriteToDelete.setId(publication.getId());
                    db.favoritePublicationDao().delete(favoriteToDelete);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    public static class PublicationViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvDescription;
        public ImageView ivImage;
        public CheckBox cbFavorite;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvPublicationAuthor);
            tvDescription = itemView.findViewById(R.id.tvPublicationDescription);
            ivImage = itemView.findViewById(R.id.ivPublicationImage);
            cbFavorite = itemView.findViewById(R.id.cb_favorite);
        }
    }
}