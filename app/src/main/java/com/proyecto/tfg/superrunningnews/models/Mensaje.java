package com.proyecto.tfg.superrunningnews.models;

import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class Mensaje implements MessageContentType {

    private String id;
    private String text;
    private Date createdAt;
    private Usuario user;

    public Mensaje() {
    }

    public Mensaje(String id, Usuario user, String text) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = new Date();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public IUser getUser() {
        return this.user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }

}