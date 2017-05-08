package com.proyecto.tfg.superrunningnews.models;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

/**
 * Created by SusVa on 5/05/17.
 */

public class Dialog implements IDialog<Message> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Usuario> users;
    private Message lastMessage;

    private int unreadCount;

    public Dialog(String id, String name, String photo,
                  ArrayList<Usuario> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<Usuario> getUsers() {
        return users;
    }

    @Override
    public Message getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

}
