package com.proyecto.tfg.superrunningnews.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Mensaje;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

/**
 * Created by SusVa on 28/05/17.
 */

public class MessageAdapter extends MessagesListAdapter.IncomingMessageViewHolder<Mensaje> {

    public MessageAdapter(View itemView) {
        super(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBind(Mensaje mensaje) {
        super.onBind(mensaje);
        ((TextView) itemView.findViewById(R.id.username)).setText("\t\t\t" + mensaje.getUser().getName());
    }
}