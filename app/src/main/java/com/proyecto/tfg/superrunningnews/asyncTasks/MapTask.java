package com.proyecto.tfg.superrunningnews.asyncTasks;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapTask extends AsyncTask<Void, Void, Void> {

    private List<MarkerOptions> marcadores;
    private List<Noticia> tNoticia;

    public MapTask(List<Noticia> tNoticia, List<MarkerOptions> tMarcadores) {
        this.tNoticia = tNoticia;
        marcadores = tMarcadores;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (Noticia noticia : tNoticia) {
            if (noticia.getLatLng() != null) {
                MarkerOptions markerOptions = crearMarcador(noticia);
                esFavorito(markerOptions, noticia);
                marcadores.add(markerOptions);
            }
        }

        return null;
    }

    private MarkerOptions crearMarcador(Noticia noticia) {
        return new MarkerOptions()
                .position(noticia.getLatLng())
                .title(noticia.getTitulo())
                .snippet(noticia.getLocalizacion() + " el " + noticia.getFecha())
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }

    private void esFavorito(MarkerOptions markerOptions, Noticia noticia) {
        if (noticia.isFavorito()) {
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
    }

}