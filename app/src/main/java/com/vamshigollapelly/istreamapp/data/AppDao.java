package com.vamshigollapelly.istreamapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AppDao {

    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Insert
    void addToPlaylist(PlaylistItem item);

    @Query("SELECT * FROM playlist WHERE userId = :userId")
    List<PlaylistItem> getPlaylist(int userId);
}