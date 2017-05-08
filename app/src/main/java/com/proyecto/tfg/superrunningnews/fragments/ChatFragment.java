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

package com.proyecto.tfg.superrunningnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.tfg.superrunningnews.DefaultMessagesActivity;
import com.proyecto.tfg.superrunningnews.DialogsFixtures;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.proyecto.tfg.superrunningnews.models.Message;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;

public class ChatFragment extends DemoDialogFragment {

    private ArrayList<Dialog> dialogs;
    private DialogsList dialogsList;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        dialogsList = (DialogsList) v.findViewById(R.id.dialogsList);

        // Crear grupos.
        // Concretamente 1.
        // No me queda claro si usa la imagen del grupo en sí o la de los usuarios como aparece en la
        //  demo, creo que a partir de 2 usa la de los usuarios o algo así, investigar.
        //  Lo ideal sería poner solamente la img del evento sacada con picasso por ejemplo...
        dialogs = new ArrayList<>();
        Usuario u1 = new Usuario("1", "JesusVa", "URL avatar", true, "jesus");
        Usuario u2 = new Usuario("2", "Christian", "https://pbs.twimg.com/profile_images/719940973778722816/0vf4NGTC.jpg", false, "cristian");
        ArrayList<Usuario> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        Message m1 = new Message("1", u1, "Hola, soy un mensaje");
        Dialog d1 = new Dialog("1" , "Grupo N1 prueba", "https://pbs.twimg.com/profile_images/719940973778722816/0vf4NGTC.jpg", users, m1, 1);
        dialogs.add(d1);
        initAdapter();
        return v;
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        DefaultMessagesActivity.open(getContext());
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
        //super.dialogsAdapter.setItems(DialogsFixtures.getDialogs()); // Cargar grupos de prueba.
        // Metodo setItems, tanto para los grupos como para los mensajes
        super.dialogsAdapter.setItems(dialogs);

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }

}
