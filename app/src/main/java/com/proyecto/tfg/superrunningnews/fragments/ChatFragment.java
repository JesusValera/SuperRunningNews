package com.proyecto.tfg.superrunningnews.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.adapters.AdapterChat;
import com.proyecto.tfg.superrunningnews.models.Chat;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private Button btEnviarMensaje;
    private EditText etMensaje;
    private Handler h;
    private ArrayList<Chat> mensajes;
    private AdapterChat adapter;
    private RecyclerView rvChat;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        btEnviarMensaje = (Button) v.findViewById(R.id.btEnviarMensaje);
        etMensaje = (EditText) v.findViewById(R.id.etMensaje);

        rvChat = (RecyclerView) v.findViewById(R.id.rvChat);
        mensajes = new ArrayList<>();
        adapter = new AdapterChat(mensajes, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true);
        rvChat.setLayoutManager(manager);
        rvChat.setAdapter(adapter);

        btEnviarMensaje.setOnClickListener(ListenerEnviarMensaje);

        return v;
    }

    private Runnable respuesta = new Runnable() {
        @Override
        public void run() {
            mensajes.add(new Chat("¡Hola!", false));
            adapter.notifyDataSetChanged();
            rvChat.scrollToPosition(mensajes.size() - 1);
        }
    };

    private View.OnClickListener ListenerEnviarMensaje = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            String msg = etMensaje.getText().toString();
//
//            Toast.makeText(getContext(), "Msg: " + msg, Toast.LENGTH_SHORT).show();
//            etMensaje.setText("");

            if (!etMensaje.getText().toString().equalsIgnoreCase("")) {
                mensajes.add(new Chat(etMensaje.getText().toString(), true));
                adapter.notifyDataSetChanged();
                rvChat.scrollToPosition(mensajes.size() - 1);
                etMensaje.setText("");
                h = new Handler(Looper.getMainLooper());
                h.postDelayed(respuesta, 1000);
            }
        }
    };

}
