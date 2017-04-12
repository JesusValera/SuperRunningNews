package com.proyecto.tfg.superrunningnews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<Noticia> tNoticia;
    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient gac;

    public MapaFragment() {
        // Required empty public constructor
    }



    /**
     * Pruebas ...
     */



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.wtf("onAttach", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("onCreate", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

        // Traer array de BottomBarActivity.
        Bundle args = getArguments();
        if (args != null) {
            this.tNoticia = args.getParcelableArrayList("noticia");
        }

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        if (gac == null) {
            gac = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.wtf("onActivityCreated", "onActivityCreated");
    }

    @Override
    public void onStart() {
        if (gac != null) {
            gac.connect();
        }
        super.onStart();
        Log.wtf("onStart", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        Log.wtf("onResume", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        // Guardar informacion.
        Log.w("onPause", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("onStop", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.e("onDestroy", "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        Log.wtf("onLowMemory", "onLowMemory");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        //this.googleMap.getUiSettings().setMapToolbarEnabled(false); // Botones de como llegar cuando se pulsa snippet.
        this.googleMap.clear();

        new MapTask(MapaFragment.this.getContext(), this.googleMap, tNoticia).execute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
    }

}
