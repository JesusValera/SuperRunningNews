/* ******************************************************************************
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
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class MessagesActivity extends BaseMessagesActivity
        implements MessageInput.InputListener {

    private MessagesList messagesList;
    private DatabaseReference ref;
    private String titulo, usuario;
    private boolean haEntrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        titulo = getIntent().getStringExtra("titulo");
        haEntrado = false;
        barraSuperior();
        sharedPreferences();

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        messagesAdapter = new MessagesListAdapter<>(usuario, anadirNombreMensaje(), imageLoader);
        ((MessageInput) findViewById(R.id.input)).setInputListener(this);

        initAdapter();
    }

    private void sharedPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        usuario = pref.getString("usuario", null);
    }

    private MessageHolders anadirNombreMensaje() {
        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        return holdersConfig.setIncomingTextConfig(MessageAdapter.class, R.layout.item_custom_incoming_message);
    }

    private void initAdapter() {
        establecerAdaptador();
        // grupos/{nombreEvento}/mensajes/{user-hora}/[MENSAJE]
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        cargarTodosMensajes(db);
        mensajeNuevo(db);
    }

    private void establecerAdaptador() {
        messagesAdapter.enableSelectionMode(this);
        messagesList.setAdapter(messagesAdapter);
    }

    private void cargarTodosMensajes(FirebaseDatabase db) {
        DatabaseReference ref2 = db.getReference("grupos/").child(titulo + "/").child("mensajes/");
        ref2.addListenerForSingleValueEvent(ref2_ValueEvent);
    }

    private void mensajeNuevo(FirebaseDatabase db) {
        ref = db.getReference("grupos/").child(titulo + "/").child("mensajes/");
        ref.addValueEventListener(ref_ValueEvent);
    }

    private ValueEventListener ref_ValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            anadirMensaje(obtenerUltimoMensaje(dataSnapshot));
        }

        private void anadirMensaje(Mensaje mensaje) {
            if (deberiaAnadirMensaje(mensaje)) {
                messagesAdapter.addToStart(mensaje, true);
            }
            haEntrado = true;
        }

        private boolean deberiaAnadirMensaje(Mensaje mensaje) {
            return (haEntrado || messagesAdapter.getItemCount() == 0)
                    && !mensaje.estaVacio();
        }

        private Mensaje obtenerUltimoMensaje(DataSnapshot dataSnapshot) {
            Mensaje mensaje = Mensaje.createEmtpy();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                mensaje = data.getValue(Mensaje.class);
            }
            return mensaje;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void barraSuperior() {
        Toolbar toolbar = crearYEstablecerToolbar();
        try {
            anadirConfigs(toolbar);
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(),
                    "Error al cargar chat",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private Toolbar crearYEstablecerToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void anadirConfigs(Toolbar toolbar) throws NullPointerException {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        long date = new Date().getTime();
        Mensaje sms = new Mensaje(String.valueOf(date), getUsuario(), input.toString());
        ref.child(date + "-" + usuario).setValue(sms);

        return true;
    }

    private Usuario getUsuario() {
        return new Usuario(usuario, usuario, "https://firebasestorage.googleapis.com/" +
                "v0/b/superrunningnews-75380.appspot.com/o/imagenes%2F" + usuario + ".png?alt=media");
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