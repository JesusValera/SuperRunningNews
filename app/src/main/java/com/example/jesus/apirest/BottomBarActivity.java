package com.example.jesus.apirest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jesus.apirest.fragments.FragmentMapa;
import com.example.jesus.apirest.fragments.FragmentFeed;
import com.example.jesus.apirest.fragments.FragmentPerfil;
import com.example.jesus.apirest.models.Noticia;

import java.util.ArrayList;

public class BottomBarActivity extends AppCompatActivity {

    private ArrayList<Noticia> noticias;
    private int seccion;
    // Si es la primera vez que se ejecuta la aplicación la condicion (linea 50~) no permite
    //  que entre en el switch y muestra una pantalla en blanco.
    private boolean primeraVez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        seccion = R.id.navigation_noticias;
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent i = getIntent();
        this.noticias = i.getParcelableArrayListExtra("noticia");

        // Cargar por defecto la pantalla de noticias.
        navigation.setSelectedItemId(seccion);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_noticias:

                    if (seccion != R.id.navigation_noticias || primeraVez) {
                        FragmentFeed noticiaFragment = new FragmentFeed();
                        Bundle bundleFeed = new Bundle();
                        bundleFeed.putParcelableArrayList("noticia", noticias);
                        noticiaFragment.setArguments(bundleFeed);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, noticiaFragment).commit();

                        primeraVez = false;
                        seccion = R.id.navigation_noticias;
                    }

                    return true;
                case R.id.navigation_mapa:

                    if (seccion != R.id.navigation_mapa) {
                        FragmentMapa mapaFragment = new FragmentMapa();
                        Bundle bundleMap = new Bundle();
                        bundleMap.putParcelableArrayList("noticia", noticias);
                        mapaFragment.setArguments(bundleMap);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, mapaFragment).commit();
                        seccion = R.id.navigation_mapa;
                    } else {
                        Toast.makeText(getApplicationContext(), "¿Es que eres tonto o qué?", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case R.id.navigation_perfil:

                    if (seccion != R.id.navigation_perfil) {
                        FragmentPerfil perfilFragment = new FragmentPerfil();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, perfilFragment).commit();
                        seccion = R.id.navigation_perfil;
                    }

                    return true;
            }
            return false;
        }

    };

}
