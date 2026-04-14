package com.vamshigollapelly.istreamapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vamshigollapelly.istreamapp.R;
import com.vamshigollapelly.istreamapp.data.PlaylistItem;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PlaylistItem item);
    }

    private List<PlaylistItem> items;
    private OnItemClickListener listener;

    public PlaylistAdapter(List<PlaylistItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUrl;
        public ViewHolder(View view) {
            super(view);
            tvUrl = view.findViewById(R.id.tvVideoUrl);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.tvUrl.setText(item.videoUrl);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}