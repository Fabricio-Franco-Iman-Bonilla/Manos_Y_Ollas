package com.example.manosyollas.controladores;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manosyollas.R;
import com.example.manosyollas.actividades.ImageAdapter;
import com.example.manosyollas.clases.ForumItem;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private List<ForumItem> forumItems;

    private OnItemClickListener listener;

    // Interfaz para manejar los clics en cada item
    public interface OnItemClickListener {
        void onItemClick(ForumItem foro);
    }

    public ForumAdapter(List<ForumItem> forumItems,OnItemClickListener listener) {
        this.forumItems = forumItems;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_forum, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        ForumItem currentItem = forumItems.get(position);
        holder.bind(currentItem,listener);
        holder.iconImageView.setImageResource(currentItem.getIconResId());
        holder.titleTextView.setText(currentItem.getTitle());
        holder.descriptionTextView.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return forumItems.size();
    }

    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconImageView;

        public TextView titleTextView;
        public TextView descriptionTextView;

        public ForumViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.forum_icon);
            titleTextView = itemView.findViewById(R.id.forum_title);
            descriptionTextView = itemView.findViewById(R.id.forum_supporting_text);
        }

        public void bind(final ForumItem foro, final OnItemClickListener listener) {
            titleTextView.setText(foro.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(foro);
                }
            });
        }
        //Probando comentario
    }
}

