package com.tuanvu.chatbox;

import java.io.Serializable;

/**
 * Created by com.tuanvu.chatbox on 02/08/2017.
 */

public class Users implements Serializable {

    String uid;
    String account;
    String displayName;
    String avatar;
    boolean gender;

    public Users() {
    }

    public Users(String uid, String account, String displayName, String avatar, boolean gender) {
        this.uid = uid;
        this.account = account;
        this.displayName = displayName;
        this.avatar = avatar;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String stringGender() {
        return isGender() ? "Male" : "Female";
    }
}
