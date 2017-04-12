package com.proyecto.tfg.superrunningnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.ViewHolder> {

    private List<Noticia> tNoticias;
    private Context context;
    private View.OnClickListener listener;
    private int itemPos = -1;

    public AdapterNoticia(List<Noticia> tNoticias, Context context) {
        this.tNoticias = tNoticias;
        this.context = context;
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
        return tNoticias.get(pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return tNoticias.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setItem(tNoticias.get(i));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImagen;
        private TextView txtTitulo;
        private TextView txtLocalizacion;
        private TextView txtFecha;

        private ViewHolder(final View v) {
            super(v);
            ivImagen = (ImageView) v.findViewById(R.id.ivImagen);
            txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);
            txtLocalizacion = (TextView) v.findViewById(R.id.txtLocalizacion);
            txtFecha = (TextView) v.findViewById(R.id.txtFecha);
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

        public void setItem(Noticia n) {
            Picasso.with(context).load(n.getImagen()).into(ivImagen);
            txtTitulo.setText(n.getTitulo());
            txtLocalizacion.setText(n.getLocalizacion());
            txtFecha.setText(n.getFecha());
        }
    }
}
