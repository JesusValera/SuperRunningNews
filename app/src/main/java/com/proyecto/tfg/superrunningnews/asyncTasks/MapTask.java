package com.proyecto.tfg.superrunningnews.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private LatLngBounds.Builder builder;
    private GoogleMap googleMap;
    private ProgressDialog progressDialog;
    private List<MarkerOptions> marcadores;
    private List<Noticia> tNoticia;
    private MapView mMapView;

    public MapTask(Context context, GoogleMap googleMap, List<Noticia> tNoticia, LatLngBounds.Builder builder, MapView mMapView) {
        this.context = context;
        this.googleMap = googleMap;
        this.tNoticia = tNoticia;
        marcadores = new ArrayList<>();
        this.builder = builder;
        this.mMapView = mMapView;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
    }

    @Override
    protected void onPreExecute() {

        mMapView.setVisibility(View.INVISIBLE);

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
        try {
            for (Noticia noticia : tNoticia) {
                List<Address> pos = geocoder.getFromLocationName(noticia.getLocalizacion(), 1);
                LatLng latLng = new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude());
                // Texto, descripcicon [y color] del marcador.
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(noticia.getTitulo())
                        .snippet(noticia.getLocalizacion() + " el " + noticia.getFecha())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                if(noticia.isFavorito()) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
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

        mMapView.setVisibility(View.VISIBLE);

        // Anadir los marcadores. ¡No se puede hacer en doInBackground!
        for (MarkerOptions m : marcadores) {
            googleMap.addMarker(m);
        }
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

        super.onPostExecute(aVoid);
    }

}