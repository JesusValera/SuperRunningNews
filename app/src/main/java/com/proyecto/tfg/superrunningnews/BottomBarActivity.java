package com.proyecto.tfg.superrunningnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.fragments.ChatFragment;
import com.proyecto.tfg.superrunningnews.fragments.MapaFragment;
import com.proyecto.tfg.superrunningnews.fragments.NoticiaFragment;
import com.proyecto.tfg.superrunningnews.fragments.PerfilFragment;
import com.proyecto.tfg.superrunningnews.models.Noticia;

import java.util.ArrayList;

public class BottomBarActivity extends AppCompatActivity {

    private final MapaFragment FRAG_MAPA = new MapaFragment();
    private final NoticiaFragment FRAG_NOTICIA = new NoticiaFragment();
    private final PerfilFragment FRAG_PERFIL = new PerfilFragment();
    private final ChatFragment FRAG_CHAT = new ChatFragment();
    private ArrayList<Noticia> tNoticias;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    // Si es la primera vez que se ejecuta la aplicación la condicion (linea 50~) no permite
    //  que entre en el switch y muestra una pantalla en blanco.
    private boolean primeraVez = true;
    private int seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        seccion = R.id.navigation_noticias;
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent i = getIntent();
        this.tNoticias = i.getParcelableArrayListExtra("noticia");

        // Cargar por defecto la pantalla de noticias.
        navigation.setSelectedItemId(seccion);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_noticias:

                    if (seccion != R.id.navigation_noticias || primeraVez) {
                        Bundle bundleFeed = new Bundle();
                        bundleFeed.putParcelableArrayList("noticia", tNoticias);
                        FRAG_NOTICIA.setArguments(bundleFeed);
                        fragmentTransaction.replace(R.id.content, FRAG_NOTICIA).commit();
                        seccion = R.id.navigation_noticias;
                        primeraVez = false;
                    }

                    return true;
                case R.id.navigation_mapa:

                    if (seccion != R.id.navigation_mapa) {
                        Bundle bundleMap = new Bundle();
                        bundleMap.putParcelableArrayList("noticia", tNoticias);
                        FRAG_MAPA.setArguments(bundleMap);
                        fragmentTransaction.replace(R.id.content, FRAG_MAPA).commit();
                        seccion = R.id.navigation_mapa;
                    }

                    return true;
                case R.id.navigation_perfil:
                    // NO Pasar la coleccion de noticias, sino objeto Usuario para contruir FRAG_PERFIL.
                    if (seccion != R.id.navigation_perfil) {
                        fragmentTransaction.replace(R.id.content, FRAG_PERFIL).commit();
                        seccion = R.id.navigation_perfil;
                    }

                    return true;

                case R.id.navigation_chat:
                    // Si es mucho trabajo, comentar esto y ocultar boton Chat...
                    if (seccion != R.id.navigation_chat) {
                        fragmentTransaction.replace(R.id.content, FRAG_CHAT).commit();
                        seccion = R.id.navigation_chat;
                    }

                    return true;
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            moveTaskToBack(true);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Presione una vez más para salir.", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
