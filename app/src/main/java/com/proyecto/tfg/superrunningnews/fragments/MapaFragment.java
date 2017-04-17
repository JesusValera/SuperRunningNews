package com.proyecto.tfg.superrunningnews.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.proyecto.tfg.superrunningnews.asyncTasks.MapTask;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class MapaFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<Noticia> tNoticia;
    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient gac;
    private LatLngBounds.Builder builder;

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

        Bundle args = getArguments();
        if (args != null) {
            this.tNoticia = args.getParcelableArrayList("noticia");
        }

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately.
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        builder = new LatLngBounds.Builder();

        if (gac == null) {
            gac = new GoogleApiClient.Builder(getContext())
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return v;
    }

    @Override
    public void onStart() {
        if (gac != null) {
            gac.connect();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.setOnMapLoadedCallback(mapLoadedListener);

        new MapTask(MapaFragment.this.getContext(), googleMap, tNoticia, builder).execute();
    }

    private GoogleMap.OnMapLoadedCallback mapLoadedListener = new GoogleMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "La conexión falló.", Toast.LENGTH_SHORT).show();
    }

}
