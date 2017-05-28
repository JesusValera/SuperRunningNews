package com.proyecto.tfg.superrunningnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proyecto.tfg.superrunningnews.activities.MessagesActivity;
import com.proyecto.tfg.superrunningnews.activities.SplashActivity;
import com.proyecto.tfg.superrunningnews.models.Favorito;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.ViewHolder> {

    private List<Noticia> tNoticias;
    private Context context;
    private View.OnClickListener listener;
    private int itemPos = -1;
    private DatabaseReference ref;
    private String usuario;
    private final String format = "dd/MM/yyyy";
    private final String fechaHoy = new SimpleDateFormat(format, new Locale("ES", "es"))
            .format(Calendar.getInstance().getTime());

    public NoticiaAdapter(List<Noticia> tNoticias, Context context) {
        this.tNoticias = tNoticias;
        this.context = context;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usuario = SplashActivity.pref.getString("usuario", null);
        ref = db.getReference("favoritos/" + usuario);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public int getItemPos() {
        return itemPos;
    }

    public void setItemPos(int pos) {
        this.itemPos = pos;
    }

    public Noticia getItem(int pos) {
        if (pos != -1) {
            return tNoticias.get(pos);
        }

        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        if (null == tNoticias) {
            return 0;
        }

        return tNoticias.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(tNoticias.get(i));
        final Noticia noticia = tNoticias.get(i);
        viewHolder.fbFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noticia.isFavorito()) {
                    noticia.setFavorito(false);
                    ref.child(noticia.getTitulo()).removeValue();
                } else {
                    ref.child(noticia.getTitulo()).setValue(new Favorito(usuario, noticia.getTitulo()));
                    noticia.setFavorito(true);
                }
                notifyDataSetChanged();
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImagen;
        private TextView txtTitulo;
        private TextView txtLocalizacion;
        private TextView txtFecha;
        private LinearLayout llInformacion;
        private FloatingActionButton fbFavorito;
        private FloatingActionButton fbChat;

        private ViewHolder(final View v) {
            super(v);
            ivImagen = (ImageView) v.findViewById(R.id.ivImagen);
            txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);
            txtLocalizacion = (TextView) v.findViewById(R.id.txtLocalizacion);
            txtFecha = (TextView) v.findViewById(R.id.txtFecha);
            llInformacion = (LinearLayout) v.findViewById(R.id.llInformacion);
            fbFavorito = (FloatingActionButton) v.findViewById(R.id.fbFavorito);
            fbChat = (FloatingActionButton) v.findViewById(R.id.fbChat);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (itemPos == -1 || itemPos != pos)
                        itemPos = pos;
                    else
                        itemPos = -1;
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
        }

        public void setItem(final Noticia n) {
            Picasso.with(context).load(n.getImagen()).into(ivImagen);
            txtTitulo.setText(n.getTitulo());
            txtLocalizacion.setText(n.getLocalizacion());
            txtFecha.setText(n.getFecha());

            if (n.isFavorito()) fbFavorito.setImageResource(R.drawable.ic_favorite_black_48dp);
            else fbFavorito.setImageResource(R.drawable.ic_favorite_border_black_48dp);

            DateFormat formatter = new SimpleDateFormat(format, new Locale("ES", "es"));
            Date fecha;
            try {
                fecha = formatter.parse(n.getFecha());
            } catch (ParseException e) {
                fecha = Calendar.getInstance().getTime();
            }

            if (n.getFecha().equals(fechaHoy)) {
                llInformacion.setBackgroundColor(Color.YELLOW);
            } else if (fecha.before(Calendar.getInstance().getTime())) {
                llInformacion.setBackgroundColor(Color.rgb(230, 230, 230));
            } else {
                // Si no se pone esta linea se pintan todos.
                llInformacion.setBackgroundColor(Color.WHITE);
            }

            fbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MessagesActivity.class);
                    i.putExtra("titulo", n.getTitulo());
                    context.startActivity(i);
                }
            });
        }
    }
}
