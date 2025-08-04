package com.svalero.memazo.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.svalero.memazo.domain.FavoritePublication;

@Database(entities = {FavoritePublication.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoritePublicationDao favoritePublicationDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "memazo.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}