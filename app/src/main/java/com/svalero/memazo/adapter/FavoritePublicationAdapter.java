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
import com.svalero.memazo.domain.Publication; // Se mantiene por el ViewHolder

import java.util.List;


public class FavoritePublicationAdapter extends RecyclerView.Adapter<FavoritePublicationAdapter.PublicationViewHolder> {
    private Context context;
    private List<FavoritePublication> favoritePublications;

    public FavoritePublicationAdapter(Context context, List<FavoritePublication> favoritePublications) {
        this.context = context;
        this.favoritePublications = favoritePublications;
    }

    public void setFavorites(List<FavoritePublication> favoritePublications) {
        this.favoritePublications = favoritePublications;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        FavoritePublication favorite = favoritePublications.get(position);

        holder.tvAuthor.setText(favorite.getUserName());
        holder.tvDescription.setText(favorite.getContent());
        Glide.with(holder.itemView.getContext())
                .load(favorite.getImageUrl())
                .fallback(R.drawable.ic_image_not_found)
                .error(R.drawable.ic_image_not_found)
                .into(holder.ivImage);

        holder.cbFavorite.setChecked(true);

        holder.cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(context);
                    db.favoritePublicationDao().delete(favorite);

                    new Handler(Looper.getMainLooper()).post(() -> {
                        favoritePublications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, favoritePublications.size());
                    });
                });
            }
        });
    }
    @Override
    public int getItemCount() {

        return favoritePublications.size();
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