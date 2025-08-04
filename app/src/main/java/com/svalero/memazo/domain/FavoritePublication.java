package com.svalero.memazo.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavoritePublication")
public class FavoritePublication {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String userName;
    private String content;
    private String imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
