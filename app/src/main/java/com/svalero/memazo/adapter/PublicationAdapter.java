package com.svalero.memazo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.domain.Publication;
import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    private List<Publication> publications;

    public PublicationAdapter(List<Publication> publications) {
        this.publications = publications;
    }

    // Método para actualizar la lista de datos del adapter
    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    /**
     * Método llamado para crear una nueva fila (ViewHolder).
     * Aquí se "infla" el layout XML de cada item.
     */
    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    /**
     * Método llamado para enlazar los datos de una publicación con una fila (ViewHolder).
     */
    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        Publication publication = publications.get(position);

        holder.tvAuthor.setText(publication.getUserName());
        holder.tvDescription.setText(publication.getContent());

        Glide.with(holder.itemView.getContext())
                .load(publication.getImageUrl())
                .into(holder.ivImage);
    }

    /**
     * Método que devuelve el número total de items en la lista.
     */
    @Override
    public int getItemCount() {
        return publications.size();
    }


    /**
     * Clase interna que representa una fila y guarda las referencias a sus vistas.
     */
    public static class PublicationViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvDescription;
        public ImageView ivImage;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvPublicationAuthor);
            tvDescription = itemView.findViewById(R.id.tvPublicationDescription);
            ivImage = itemView.findViewById(R.id.ivPublicationImage);
        }
    }
}