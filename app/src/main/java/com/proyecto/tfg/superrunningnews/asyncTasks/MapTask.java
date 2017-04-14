package com.proyecto.tfg.superrunningnews.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SusVa on 11/04/17.
 */

public class MapTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private LatLngBounds.Builder builder;
    private GoogleMap googleMap;
    private ProgressDialog progressDialog;
    private List<MarkerOptions> marcadores;
    private List<Noticia> tNoticia;

    public MapTask(Context context, GoogleMap googleMap, List<Noticia> tNoticia) {
        this.context = context;
        this.googleMap = googleMap;
        this.tNoticia = tNoticia;
        marcadores = new ArrayList<>();
        builder = new LatLngBounds.Builder();
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false); // No es cancelable. >:D
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cargando mapa...");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Geocoder geocoder = new Geocoder(context, new Locale("es", "ES"));
        MarkerOptions markerOptions;
        try {
            for (Noticia noticia : tNoticia) {
                List<Address> pos = geocoder.getFromLocationName(noticia.getLocalizacion(), 1);
                LatLng latLng = new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude());
                // Texto, descripcicon [y color] del marcador.
                markerOptions = new MarkerOptions().position(latLng).title(noticia.getTitulo())
                        .snippet(noticia.getLocalizacion() + " el " + noticia.getFecha());
                marcadores.add(markerOptions);
                builder.include(latLng);
            }
        } catch (IOException e) {
            Toast.makeText(context, "Hubo un error al cargar una de las localizaciones: " + e.getCause(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        // Anadir los marcadores. ¡No se puede hacer en doInBackground!
        for (MarkerOptions m : marcadores) {
            googleMap.addMarker(m);
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

        super.onPostExecute(aVoid);
    }

}