package com.proyecto.tfg.superrunningnews.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private List<MarkerOptions> marcadores;
    private List<Noticia> tNoticia;

    public MapTask(Context context, List<Noticia> tNoticia, List<MarkerOptions> tMarcadores) {
        this.context = context;
        this.tNoticia = tNoticia;
        marcadores = tMarcadores;
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (Noticia noticia : tNoticia) {
            // Texto, descripcion y color del marcador.
            if (noticia.getLatLng() != null) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(noticia.getLatLng())
                        .title(noticia.getTitulo())
                        .snippet(noticia.getLocalizacion() + " el " + noticia.getFecha())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                if (noticia.isFavorito()) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                marcadores.add(markerOptions);
            }
        }

        return null;
    }

}