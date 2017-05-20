package com.proyecto.tfg.superrunningnews;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
    private int seccion;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ArrayList<Noticia> tNoticias = getIntent().getParcelableArrayListExtra("noticia");

        Bundle bundleNoticia = new Bundle();
        bundleNoticia.putParcelableArrayList("noticia", tNoticias);
        FRAG_NOTICIA.setArguments(bundleNoticia);
        FRAG_MAPA.setArguments(bundleNoticia);
        FRAG_CHAT.setArguments(bundleNoticia);

        if (savedInstanceState != null) {
            seccion = savedInstanceState.getInt("seccionActual");
            navigation.setSelectedItemId(seccion);
        } else {
            seccion = R.id.navigation_noticias;
            navigation.setSelectedItemId(seccion);
        }

        try {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.coin);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_noticias:

                    fragmentTransaction.replace(R.id.content, FRAG_NOTICIA).commit();
                    seccion = R.id.navigation_noticias;
                    hacerScrollAPrimeraPos();

                    return true;
                case R.id.navigation_mapa:

                    fragmentTransaction.replace(R.id.content, FRAG_MAPA).commit();
                    seccion = R.id.navigation_mapa;

                    return true;
                case R.id.navigation_perfil:

                    fragmentTransaction.replace(R.id.content, FRAG_PERFIL).commit();
                    seccion = R.id.navigation_perfil;

                    return true;
                case R.id.navigation_chat:

                    fragmentTransaction.replace(R.id.content, FRAG_CHAT).commit();
                    seccion = R.id.navigation_chat;

                    return true;
            }

            return false;
        }

    };

    private void hacerScrollAPrimeraPos() {
        try {
            ((RecyclerView) FRAG_NOTICIA.getActivity().findViewById(R.id.recyclerView)).smoothScrollToPosition(0);
        } catch (NullPointerException e) {
            ;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("seccionActual", seccion);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            moveTaskToBack(true);
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.salir, Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

}
