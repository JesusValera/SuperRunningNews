package com.example.jesus.apirest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jesus.apirest.Noticia;
import com.example.jesus.apirest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Susva on 11/04/17.
 */

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.FeedViewHolder>{

    private List<Noticia> datos;
    private Context context;

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView ivImagen;
        public TextView txtTitulo;
        public TextView txtLocalizacion;
        public TextView txtFecha;

        public FeedViewHolder(View v) {
            super(v);
            ivImagen = (ImageView) v.findViewById(R.id.ivImagen);
            txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);
            txtLocalizacion = (TextView) v.findViewById(R.id.txtLocalizacion);
            txtFecha = (TextView) v.findViewById(R.id.txtFecha);
        }
    }

    public AdapterFeed(List<Noticia> datos, Context context) {
        this.datos = datos;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false);
        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder viewHolder, int i) {
        Picasso.with(context).load(datos.get(i).getImagen()).into(viewHolder.ivImagen);
        viewHolder.txtTitulo.setText(datos.get(i).getTitulo());
        viewHolder.txtLocalizacion.setText(datos.get(i).getLocalizacion());
        viewHolder.txtFecha.setText(datos.get(i).getFecha()); // TODO solo estos campos o todos de obj noticia ??
    }
}
