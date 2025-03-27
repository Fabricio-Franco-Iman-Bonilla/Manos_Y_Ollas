package com.example.manosyollas.clases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private MessageRepository messageRepository; // Asumimos que tienes un repositorio
    private LiveData<List<MessageItem>> messages; // Lista de mensajes observables

    public ChatViewModel(int forumId) {
        messageRepository = new MessageRepository(); // Asegúrate de que el repositorio esté configurado
        messages = messageRepository.getMessagesByForum(forumId);
    }

    public LiveData<List<MessageItem>> getMessagesByForum(int forumId) {
        return messageRepository.getMessagesByForum(forumId);
    }

    public void insertMessage(MessageItem message) {
        messageRepository.insert(message); // Inserta el mensaje en la base de datos
    }

}

