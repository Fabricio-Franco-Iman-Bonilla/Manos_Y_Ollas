package com.example.manosyollas.controladores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manosyollas.R;
import com.example.manosyollas.clases.ForumItem;
import com.example.manosyollas.clases.OllaItem;

import java.util.List;

public class OllaAdapter  extends RecyclerView.Adapter<OllaAdapter.OllaViewHolder> {
    private List<OllaItem> ollaItems;

    private OllaAdapter.OnItemClickListener listener;

    // Interfaz para manejar los clics en cada item
    public interface OnItemClickListener {
        void onItemClick(OllaItem olla);
    }

    public OllaAdapter(List<OllaItem> ollaItems, OllaAdapter.OnItemClickListener listener) {
        this.ollaItems = ollaItems;
        this.listener=listener;
    }

    @NonNull
    @Override
    public OllaAdapter.OllaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_olla, parent, false);
        return new OllaAdapter.OllaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OllaViewHolder holder, int position) {
        OllaItem currentItem = ollaItems.get(position);
        holder.bind(currentItem,listener);
        //holder.iconImageView.setImageResource(currentItem.getIconResId());
        holder.titleTextView.setText(currentItem.getNombre());
        holder.zonaTextView.setText(currentItem.getZona());
        holder.ubiTextView.setText(currentItem.getDireccion());
    }

    @Override
    public int getItemCount() {
        return ollaItems.size();
    }

    public static class OllaViewHolder extends RecyclerView.ViewHolder {
        //public ImageView iconImageView;

        public TextView titleTextView;
        public TextView zonaTextView;
        public TextView ubiTextView;

        public OllaViewHolder(View itemView) {
            super(itemView);
            //iconImageView = itemView.findViewById(R.id.forum_icon);
            titleTextView = itemView.findViewById(R.id.titollaubi);
            zonaTextView = itemView.findViewById(R.id.zonaollaubi);
            ubiTextView = itemView.findViewById(R.id.ubiollaubi);
        }

        public void bind(final OllaItem olla, final OllaAdapter.OnItemClickListener listener) {
            titleTextView.setText(olla.getNombre());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(olla);
                }
            });
        }
        //Probando comentario
    }

}
