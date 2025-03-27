package com.example.manosyollas.clases;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MessageRepository {
    private MessageDao messageDao; // DAO para acceder a los mensajes

    public MessageRepository() {
        AppDatabase db; // Asegúrate de tener un singleton para la base de datos
        db = AppDatabase.getInstance();
        messageDao = db.messageDao();
    }

    public LiveData<List<MessageItem>> getMessagesByForum(int forumId) {
        return messageDao.getMessagesByForum(forumId); // Obtén los mensajes por ID de foro
    }

    public void insert(MessageItem message) {
        // Puedes usar un ExecutorService o corutinas para ejecutar esta operación en un hilo diferente
        new Thread(() -> messageDao.insert(message)).start(); // Ejecuta la inserción en un hilo separado
    }
}

