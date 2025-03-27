package com.example.manosyollas.clases;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MessageItem.class}, version = 1, exportSchema = false) // Añade tus entidades aquí
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE; // Instancia singleton

    public abstract MessageDao messageDao(); // Método para acceder al DAO de mensajes

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "chat_database") // Nombre de la base de datos
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public static AppDatabase getInstance() {
        return INSTANCE;
    }
}

