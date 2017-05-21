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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.tfg.superrunningnews.DefaultMessagesActivity;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.proyecto.tfg.superrunningnews.models.Message;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatFragment extends DemoDialogFragment {

    private DialogsList dialogsList;
    private ArrayList<Dialog> tGrupos;

    private FirebaseDatabase db;
    private DatabaseReference ref;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        tGrupos = new ArrayList<>();

        // Firebase.
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("grupos/");
        ref.addListenerForSingleValueEvent(ref_ValueEventListener);

        dialogsList = (DialogsList) v.findViewById(R.id.dialogsList);

        // grupos -> nombreEvento -> mensajes -> id+CreateAt -> objetoMensaje.

        return v;
    }

    private ValueEventListener ref_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Message lastMessage = new Message("", new Usuario(), "");
            ArrayList<Usuario> user = new ArrayList<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Dialog dialog = data.getValue(Dialog.class);
                dialog.setLastMessage(lastMessage);
                dialog.setUsers(user);
                tGrupos.add(dialog);
            }
            Collections.sort(tGrupos);

            initAdapter();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getContext(), "Error cargar dialogos.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDialogClick(Dialog dialog) {
        DefaultMessagesActivity.open(getContext());
    }

    private void initAdapter() {
        // Metodo setItems, tanto para los grupos como para los mensajes
        super.dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
        super.dialogsAdapter.setItems(tGrupos);

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
        dialogsList.scrollToPosition(tGrupos.size()-1);
    }

}
