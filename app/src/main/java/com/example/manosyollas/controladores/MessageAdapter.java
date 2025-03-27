package com.example.manosyollas.controladores;

import static com.example.manosyollas.fragmentos.PerfilFragment.convertBase64ToUri;
import static com.example.manosyollas.fragmentos.PerfilFragment.decodeBase64ToUtf8;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manosyollas.R;
import com.example.manosyollas.clases.MessageItem;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    public List<MessageItem> getMessageList() {
        return messageList;
    }


    public void setMessageList(List<MessageItem> messageList) {
        this.messageList = messageList;
    }

    private List<MessageItem> messageList;
    private static final int VIEW_TYPE_RIGHT = 1;
    private static final int VIEW_TYPE_LEFT = 2;
    private String currentUserId; // ID del usuario actual

    public MessageAdapter(List<MessageItem> messageList,String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        // Determinar el tipo de vista según el ID del usuario
        MessageItem message = messageList.get(position);
        if (message.getUserId().equals(currentUserId)) {
            return VIEW_TYPE_RIGHT; // Mensaje del usuario actual
        } else {
            return VIEW_TYPE_LEFT; // Mensaje de otro usuario
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_right, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem messageItem = messageList.get(position);
        holder.txtMessageContent.setText(messageItem.getContent());
        holder.txtMessageTimestamp.setText(messageItem.getTimestamp());
        holder.txtUserName.setText(messageItem.getUserName());  // Mostrar el nombre de usuario

        String userUrl = messageItem.getUserProfileImage();

        String intermedio= decodeBase64ToUtf8(userUrl);
        Uri uri= convertBase64ToUri(intermedio);

        // Cargar la imagen de perfil usando Glide
        holder.imgUserProfile.setImageURI(uri);


    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMessageContent;
        public TextView txtMessageTimestamp;
        public TextView txtUserName;  // Nuevo TextView para el nombre del usuario
        public ImageView imgUserProfile;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageContent = itemView.findViewById(R.id.txtMessageContent);
            txtMessageTimestamp = itemView.findViewById(R.id.txtMessageTimestamp);
            txtUserName = itemView.findViewById(R.id.txtUserName);  // Asignar el TextView del nombre del usuario
            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
        }
    }
}
