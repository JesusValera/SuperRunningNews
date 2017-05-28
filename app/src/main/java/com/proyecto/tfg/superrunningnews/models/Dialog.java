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
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

public class Dialog implements IDialog<Mensaje>, Comparable<Dialog> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<Usuario> users;
    private Mensaje lastMensaje;

    private int unreadCount;

    // Constructor para firebase.
    public Dialog() {
    }

    public Dialog(String id, String name, String photo,
                  ArrayList<Usuario> users, Mensaje lastMensaje, int unreadCount) {
        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMensaje = lastMensaje;
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
    public List<? extends IUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Usuario> users) {
        this.users = users;
    }

    @Exclude
    @Override
    public Mensaje getLastMessage() {
        return lastMensaje;
    }

    @Override
    public void setLastMessage(Mensaje mensaje) {
        this.lastMensaje = mensaje;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    @Override
    public int compareTo(@NonNull Dialog o) {
        return o.getDialogName().compareToIgnoreCase(dialogName);
    }

    // Necesario para comparar los objetos cuando se hace -->
    //  Collection<c>.removeAll(<c> Collection);
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dialog)) {
            return false;
        }
        Dialog otherMember = (Dialog) obj;
        return otherMember.getId().equals(getId());
    }

}
