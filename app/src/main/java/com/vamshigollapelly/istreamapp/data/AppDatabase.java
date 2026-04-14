package com.vamshigollapelly.istreamapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, PlaylistItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AppDao appDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "istream_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
