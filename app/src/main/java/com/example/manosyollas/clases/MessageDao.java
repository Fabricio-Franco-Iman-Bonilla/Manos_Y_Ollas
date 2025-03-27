package com.example.manosyollas.clases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MessageItem message);

    @Query("SELECT * FROM ChatMessages WHERE forumId = :forumId ORDER BY timestamp ASC")
    LiveData<List<MessageItem>> getMessagesByForum(int forumId);
}

