package com.tuanvu.chatbox;

/**
 * Created by tuanvu on 12/20/17.
 */

public class GroupChats {

    private String uid;
    private String name;
    private String avatar;

    public GroupChats() {

    }

    public GroupChats(String uid, String name, String avatar) {
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
