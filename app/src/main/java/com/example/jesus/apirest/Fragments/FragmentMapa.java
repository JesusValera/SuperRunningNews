package com.example.jesus.apirest.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jesus.apirest.Noticia;
import com.example.jesus.apirest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentMapa extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<Noticia> tNoticia;
    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient gac;

    public FragmentMapa() {
        // Required empty public constructor
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
    public void onStart() {
        if (gac != null) {
            gac.connect();
        }
        super.onStart();
    }

//    @Override
//    public void onStop() {
//        if (gac != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(gac, this);
//            gac.disconnect();
//        }
//        super.onStop();
//    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
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

    // ¿Util este método? TODO -> Edit: No parece funcionar...
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        Geocoder geocoder = new Geocoder(getContext());
        MarkerOptions markerOptions;
        try {
            for (Noticia noticia : tNoticia) {
                noticia.getLocalizacion();
                List<Address> pos = geocoder.getFromLocationName(noticia.getLocalizacion(), 1);
                LatLng latLng = new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude());
                markerOptions = new MarkerOptions().position(latLng).title(noticia.getTitulo())
                        .snippet(noticia.getLocalizacion() + "\t * " + noticia.getFecha());
                googleMap.addMarker(markerOptions);

                builder.include(latLng);
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error en geocoder: " + e.getStackTrace(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // No nos sirve porque las ubicaciones son fijas.
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
    }

}
