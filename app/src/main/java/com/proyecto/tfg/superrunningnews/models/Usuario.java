package com.proyecto.tfg.superrunningnews.models;

import com.stfalcon.chatkit.commons.models.IUser;

public class Usuario implements IUser {

    private String password;
    private String id;
    private String name;
    private String avatar;

    public Usuario() {
    }

    public Usuario(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public Usuario(String id, String name, String avatar, String password) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.password = password;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
