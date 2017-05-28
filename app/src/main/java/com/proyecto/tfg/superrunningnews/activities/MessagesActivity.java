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

package com.proyecto.tfg.superrunningnews.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.tfg.superrunningnews.base.BaseMessagesActivity;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.adapters.MessageAdapter;
import com.proyecto.tfg.superrunningnews.models.Mensaje;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class MessagesActivity extends BaseMessagesActivity
        implements MessageInput.InputListener {

    private MessagesList messagesList;
    private String titulo;
    private DatabaseReference ref;
    private String usuario;
    private boolean entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        titulo = getIntent().getStringExtra("titulo");
        barraSuperior();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        usuario = pref.getString("usuario", "");

        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncomingTextConfig(MessageAdapter.class, R.layout.item_custom_incoming_message);

        messagesList = (MessagesList) findViewById(R.id.messagesList);

        messagesAdapter = new MessagesListAdapter<>(usuario, holdersConfig, imageLoader);

        entrar = false;

        ((MessageInput) findViewById(R.id.input)).setInputListener(this);
        initAdapter();
    }

    private void initAdapter() {
        messagesAdapter.enableSelectionMode(this);
        messagesList.setAdapter(messagesAdapter);

        // grupos -> nombreEvento -> mensajes -> user+hora -> [MENSAJE]
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        // Cargar todos los mensajes.
        DatabaseReference ref2 = db.getReference("grupos/").child(titulo + "/").child("mensajes/");
        ref2.addListenerForSingleValueEvent(ref2_ValueEvent);

        // Mensaje nuevo.
        ref = db.getReference("grupos/").child(titulo + "/").child("mensajes/");
        ref.addValueEventListener(ref_ValueEvent);
    }

    private ValueEventListener ref_ValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Mensaje sms = null;
            // Obtener el ultimo mensaje de todos.
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                sms = data.getValue(Mensaje.class);
            }
            // Y anadirlo al adaptador.

            if (sms != null) {
                if (entrar) {
                    messagesAdapter.addToStart(sms, true);
                } else {
                    entrar = true;
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void barraSuperior() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(titulo);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(),
                    "Error al cargar chat",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        long date = new Date().getTime();
        Usuario u = new Usuario(usuario, usuario, "https://firebasestorage.googleapis.com/" +
                "v0/b/superrunningnews-75380.appspot.com/o/imagenes%2F" + usuario + ".png?alt=media");
        Mensaje sms = new Mensaje(String.valueOf(date), u, input.toString());

        ref.child(date + "-" + usuario).setValue(sms);

        return true;
    }

    private ValueEventListener ref2_ValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Mensaje mensaje = data.getValue(Mensaje.class);
                messagesAdapter.addToStart(mensaje, true);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Error al cargar mensajes.", Toast.LENGTH_SHORT).show();
        }
    };

}