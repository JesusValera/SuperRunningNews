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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.tfg.superrunningnews.DefaultMessagesActivity;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;

public class ChatFragment extends DemoDialogFragment {

    private ArrayList<Dialog> dialogs;
    private DialogsList dialogsList;
    private ArrayList<Noticia> tNoticia;

    private FirebaseDatabase db;
    private DatabaseReference ref;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        // Paso el array para tener las noticias (titulo, imgs...) a mano.
        Bundle args = getArguments();
        if (args != null) {
            this.tNoticia = args.getParcelableArrayList("noticia");
        }

        // Firebase.
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("chat");
        ref.addValueEventListener(ref_ValueEventListener);

        dialogsList = (DialogsList) v.findViewById(R.id.dialogsList);

        // Crear grupos.
        // Concretamente 1.
        // No me queda claro si usa la imagen del grupo en sí o la de los usuarios como aparece en la
        //  demo, creo que a partir de 2 usa la de los usuarios o algo así, investigar.
        //  Lo ideal sería poner solamente la img del evento sacada con picasso por ejemplo...
        dialogs = new ArrayList<>();
        /*Usuario u1 = new Usuario("1", "JesusVa", "URL avatar", true, "jesus");
        Usuario u2 = new Usuario("2", "Christian", "https://pbs.twimg.com/profile_images/719940973778722816/0vf4NGTC.jpg", false, "cristian");
        ArrayList<Usuario> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        Message m1 = new Message("1", u1, "Hola, soy un mensaje");
        Dialog d1 = new Dialog("1" , "Grupo N1 prueba", "https://pbs.twimg.com/profile_images/719940973778722816/0vf4NGTC.jpg", users, m1, 1);*/

        //****
        //  ¿Cómo guardo los grupos en la base de datos si necesito que ya existan para usarlos?
        //  Es decir, necesito que ya estén creados para poder usarlos pero ahora no están creados,
        //  hay algo que no logro entender... **
        //  Por cierto, aquí está la respuesta: https://github.com/stfalcon-studio/ChatKit/issues/37
        //  Me da la sensación de que no responde a lo que yo le pregunto pero no lo sé, tampoco
        //  entiendo la respuesta 100% pero creo que dice algo que nada tiene que ver.
        //  Lo que se me ocurre, traer de firebase a todos los usuarios para efectivamente, pasarle
        //  ese array al grupo, si no, no va.
        //  ¿Crear antes mensajes para añadirlos al grupo? Pero no existen los grupos... alsjfslj :)
        //
        //
        //  ** UPDATE: Crear los grupos en NoticiaFragment/BottomBarActivity/ProgressTask para
        //             cuando se de al boton del chat de una tarjeta te abra la actividad directamente.
        //   TODO.


        // ¿Esto Aquí?
        dialogs.clear();
        for (int i = 0; i < tNoticia.size(); i++) {

            //Dialog dialog = new Dialog("" + i, tNoticia.get(i).getTitulo(), tNoticia.get(i).getImagen(), null, , 0);
        }
        //dialogs.add(d1);
        initAdapter();
        return v;
    }

    private ValueEventListener ref_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // ¿O aquí? :S
            // --.
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //.
        }
    };

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
