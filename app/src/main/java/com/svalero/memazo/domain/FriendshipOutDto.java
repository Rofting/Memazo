package com.svalero.memazo.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class FriendshipOutDto implements Serializable {

    @SerializedName("id")        private long id;
    @SerializedName("status")    private String status;
    @SerializedName("createdAt") private String createdAt;

    @SerializedName("user")   private User user;
    @SerializedName("friend") private User friend;

    @SerializedName("userName")   private String userName;
    @SerializedName("friendName") private String friendName;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getFriend() { return friend; }
    public void setFriend(User friend) { this.friend = friend; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }
}
