package com.svalero.memazo.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.memazo.domain.FavoritePublication;

import java.util.List;

@Dao
public interface FavoritePublicationDao {
    @Insert
    void insert(FavoritePublication favorite);

    @Query("SELECT * FROM FavoritePublication")
    List<FavoritePublication> getAll();

    @Delete
    void delete(FavoritePublication favorite);

    @Update
    void update(FavoritePublication favorite);

    @Query("SELECT * FROM FavoritePublication WHERE id = :id")
    FavoritePublication getById(long id);
}
