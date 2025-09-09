package com.svalero.memazo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.db.AppDatabase;
import com.svalero.memazo.db.FavoritePublicationDao;
import com.svalero.memazo.domain.FavoritePublication;

import java.util.ArrayList;
import java.util.List;

public class FavoritePublicationAdapter extends RecyclerView.Adapter<FavoritePublicationAdapter.PublicationViewHolder> {

    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private List<FavoritePublication> favorites = new ArrayList<>();

    public FavoritePublicationAdapter(Context context, List<FavoritePublication> initial) {
        this.context = context;
        setFavorites(initial);
    }

    public void setFavorites(List<FavoritePublication> favoritePublications) {
        this.favorites = (favoritePublications != null) ? sanitize(favoritePublications) : new ArrayList<>();
        notifyDataSetChanged();
    }

    private List<FavoritePublication> sanitize(List<FavoritePublication> list) {
        List<FavoritePublication> out = new ArrayList<>();
        for (FavoritePublication f : list) if (f != null) out.add(f);
        return out;
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
        FavoritePublication fav = favorites.get(position);

        holder.tvAuthor.setText(safe(fav.getUserName(), "—"));

        String desc = safe(fav.getContent(), "");
        if (!TextUtils.isEmpty(fav.getNote())) {
            desc = desc.isEmpty() ? fav.getNote() : desc + "\n(" + fav.getNote() + ")";
        }
        holder.tvDescription.setText(desc);

        Glide.with(holder.itemView.getContext())
                .load(fav.getImageUrl())
                .fallback(R.drawable.ic_image_not_found)
                .error(R.drawable.ic_image_not_found)
                .into(holder.ivImage);

        // El checkbox en favoritos comienza marcado
        holder.cbFavorite.setOnCheckedChangeListener(null);
        holder.cbFavorite.setChecked(true);

        holder.cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    try {
                        AppDatabase db = AppDatabase.getInstance(context);
                        db.favoritePublicationDao().delete(fav);

                        mainHandler.post(() -> {
                            int adapterPos = holder.getBindingAdapterPosition();
                            if (adapterPos != RecyclerView.NO_POSITION) {
                                favorites.remove(adapterPos);
                                notifyItemRemoved(adapterPos);
                            } else {
                                notifyDataSetChanged();
                            }
                            Toast.makeText(context, R.string.favorite_deleted_ok, Toast.LENGTH_SHORT).show();
                        });
                    } catch (Exception e) {
                        mainHandler.post(() ->
                                Toast.makeText(context, R.string.favorite_update_error, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            final EditText input = new EditText(holder.itemView.getContext());
            input.setHint(R.string.edit_favorite_hint);
            input.setText(fav.getNote());

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle(R.string.edit_favorite_title)
                    .setView(input)
                    .setPositiveButton(R.string.action_save, (d, w) -> {
                        String newNote = input.getText().toString();

                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            try {
                                AppDatabase db = AppDatabase.getInstance(context);
                                FavoritePublicationDao dao = db.favoritePublicationDao();
                                fav.setNote(newNote);
                                dao.update(fav);

                                mainHandler.post(() -> {
                                    int pos = holder.getBindingAdapterPosition();
                                    if (pos != RecyclerView.NO_POSITION) {
                                        notifyItemChanged(pos);
                                    } else {
                                        notifyDataSetChanged();
                                    }
                                    Toast.makeText(context, R.string.favorite_updated, Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                mainHandler.post(() ->
                                        Toast.makeText(context, R.string.favorite_update_error, Toast.LENGTH_SHORT).show());
                            }
                        });
                    })
                    .setNegativeButton(R.string.action_cancel, null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return favorites != null ? favorites.size() : 0;
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAuthor;
        final TextView tvDescription;
        final ImageView ivImage;
        final CheckBox cbFavorite;

        PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvPublicationAuthor);
            tvDescription = itemView.findViewById(R.id.tvPublicationDescription);
            ivImage = itemView.findViewById(R.id.ivPublicationImage);
            cbFavorite = itemView.findViewById(R.id.cb_favorite);
        }
    }

    private static String safe(String s, String def) {
        return (s == null || s.trim().isEmpty()) ? def : s;
    }
}
