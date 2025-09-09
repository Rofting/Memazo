package com.svalero.memazo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.svalero.memazo.R;
import com.svalero.memazo.domain.FriendshipOutDto;
import com.svalero.memazo.domain.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    public interface OnFriendActionListener {
        void onDeleteRequest(FriendshipOutDto friendship, int position);
    }

    private final Context context;
    private final long currentUserId;
    private final OnFriendActionListener listener;
    private List<FriendshipOutDto> friendships = new ArrayList<>();

    public FriendsAdapter(Context context, long currentUserId, OnFriendActionListener listener) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    public void setFriendships(List<FriendshipOutDto> friendships) {
        this.friendships = (friendships != null) ? friendships : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        if (position >= 0 && position < friendships.size()) {
            friendships.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendshipOutDto f = friendships.get(position);

        User u = f.getUser();
        User fr = f.getFriend();
        if (u != null && fr != null) {
            User other = (u.getId() == currentUserId) ? fr : u;
            holder.tvFriendName.setText(other.getName() != null ? other.getName() : "—");
            Glide.with(holder.itemView.getContext())
                    .load(other.getAvatar())
                    .error(R.drawable.ic_profile)
                    .fallback(R.drawable.ic_profile)
                    .into(holder.ivFriendAvatar);
        } else {
            String name = f.getFriendName() != null ? f.getFriendName() : f.getUserName();
            holder.tvFriendName.setText((name != null && !name.trim().isEmpty()) ? name : "—");
            holder.ivFriendAvatar.setImageResource(R.drawable.ic_profile);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onDeleteRequest(f, pos);
                }
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return friendships != null ? friendships.size() : 0;
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivFriendAvatar;
        final TextView tvFriendName;

        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFriendAvatar = itemView.findViewById(R.id.iv_friend_avatar);
            tvFriendName = itemView.findViewById(R.id.tv_friend_name);
        }
    }
}
