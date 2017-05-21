/*******************************************************************************
 * Copyright 2016 stfalcon.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.proyecto.tfg.superrunningnews.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

public class Dialog implements IDialog<Message>, Comparable<Dialog> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Usuario> users;
    private Message lastMessage;

    private int unreadCount;

    // Constructor vacio para firebase.
    public Dialog() {
        this.id = "";
        this.dialogName = "";
        this.dialogPhoto = "";
    }

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

    @Exclude
    @Override
    public ArrayList<Usuario> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Usuario> users) {
        this.users = users;
    }

    @Exclude
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

    // Necesario para comparar los objetos cuando se hace: Collection<c>.removeAll(<c> Collection);
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dialog)) {
            return false;
        }
        Dialog otherMember = (Dialog) obj;
        return otherMember.getId().equals(getId());
    }

    @Override
    public int compareTo(@NonNull Dialog o) {
        return o.getDialogName().compareToIgnoreCase(dialogName);
    }
}
