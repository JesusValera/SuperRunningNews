package com.example.jesus.apirest.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class FragmentMapa extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient gac;

    public FragmentMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //this.googleMap.getUiSettings().setZoomControlsEnabled(true); // Esta el boton molestando. Investigar si se pueden cambiar de pos.

        // For showing a move to my location button
//        try {
//            googleMap.setMyLocationEnabled(true);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }

        // Recuperar todas las posiciones de usuarios conectados....................................

        // For dropping a marker at a point on the Map
//        LatLng ceeim = new LatLng(38.0051, -1.165);
//        googleMap.addMarker(new MarkerOptions().position(ceeim).title("Marcador").snippet("Descripcion..."));
//
//        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(ceeim).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        if (gac != null) {
//            // Set up a Location Request.
//            LocationRequest mLocationRequest = new LocationRequest();
//            mLocationRequest.setInterval(10 * 1000);
//            mLocationRequest.setFastestInterval(10 * 1000);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            // Request Location Updades.
//            try {
//                LocationServices.FusedLocationApi.requestLocationUpdates(gac, mLocationRequest, FragmentMapa.this);
//            } catch (SecurityException e) {
//                ;
//            }
//        }

        googleMap.clear();
        ///// Mostrar todas las posiciones de usuarios conectados ..........................
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // Obtener pos de usuarios activos y guardar en array.
        double l1 = 38.005073, l2 = -1.1644974, l3 = 38.01724, l4 = -1.168988;
        LatLng latLng1 = new LatLng(l1, l2), latLng2 = new LatLng(l3, l4);

        MarkerOptions m = new MarkerOptions().position(latLng1).title(String.valueOf("Loc1")).snippet("Descripcion...");
        MarkerOptions m2 = new MarkerOptions().position(latLng2).title(String.valueOf("Loc2")).snippet("Descripcion2..");
        googleMap.addMarker(m);
        googleMap.addMarker(m2);
        builder.include(latLng1);
        builder.include(latLng2);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

//        try {
//            Location loc = LocationServices.FusedLocationApi.getLastLocation(gac);
//            if (loc != null) {
//                Toast.makeText(getContext(), "onConnected\nLAST LOCATION\nLAT: " + loc.getLatitude()
//                        + " Long: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            }
//        } catch (SecurityException e) {
//            ;
//        }
        Toast.makeText(getContext(), "onConnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

}
