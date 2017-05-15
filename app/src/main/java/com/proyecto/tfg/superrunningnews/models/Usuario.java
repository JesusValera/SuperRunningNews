package com.proyecto.tfg.superrunningnews.models;

import com.stfalcon.chatkit.commons.models.IUser;

public class Usuario implements IUser {

    /**
     * Usado: http://www.parcelabler.com/
     * Mas info: http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable
     */

    private String password;
    // array de String de las carreras marcadas como favoritas.
    // String con URL de img para chat? -> Eso si le metemos imagen al chat (propia y otro usuario, solo otro o ninguna img)... (?)
    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public Usuario() {
        this.name = "";
        this.password= "";
    }

    public Usuario(String id, String name, String avatar, boolean online, String password) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
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

    public boolean isOnline() {
        return online;
    }
}
