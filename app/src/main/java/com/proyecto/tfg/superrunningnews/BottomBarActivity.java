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
    private ArrayList<Noticia> tNoticias;
    private int seccion;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        this.tNoticias = getIntent().getParcelableArrayListExtra("noticia");

        if (savedInstanceState != null) {
            seccion = savedInstanceState.getInt("seccionActual");
            navigation.setSelectedItemId(seccion);
        } else {
            seccion = R.id.navigation_noticias;
            navigation.setSelectedItemId(seccion);
        }

        /** TODO. Se obtiene directamente desde el propio fragmento. Me gusta.
         **  Dejamos esto por aquí mientras por si, en limpieza final borrar*/
        // Pasar objeto Usuario para contruir FRAG_PERFIL (¿y Chat?).
        /*Bundle bundleUsuario = new Bundle();
        bundleUsuario.putParcelable("usuario", usuario); //--> ¡¡Usuario no es parcelable, aún!!
        FRAG_PERFIL.setArguments(bundleUsuario);
        FRAG_CHAT.setArguments(bundleUsuario); //(??)*/

        Bundle bundleNoticia = new Bundle();
        bundleNoticia.putParcelableArrayList("noticia", tNoticias);
        FRAG_NOTICIA.setArguments(bundleNoticia);
        FRAG_MAPA.setArguments(bundleNoticia);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, FRAG_NOTICIA)
                /*.add(R.id.content, FRAG_MAPA)*/
                /*.add(R.id.content, FRAG_PERFIL)*/
                .add(R.id.content, FRAG_CHAT).commit();

        try{
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.coin);
            mp.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(FRAG_NOTICIA).hide(FRAG_MAPA).hide(FRAG_PERFIL).hide(FRAG_CHAT);

            switch (item.getItemId()) {
                case R.id.navigation_noticias:

                    fragmentTransaction.show(FRAG_NOTICIA).commit();
                    seccion = R.id.navigation_noticias;
                    // Para que cuando se pulse el botón de noticias haga scroll al primer elemento.
                    hacerScroll();

                    return true;
                case R.id.navigation_mapa:

                    if (!FRAG_MAPA.isAdded())
                        fragmentTransaction.add(R.id.content, FRAG_MAPA); // <-- !!

                    fragmentTransaction.show(FRAG_MAPA).commit();
                    seccion = R.id.navigation_mapa;

                    return true;
                case R.id.navigation_perfil:

                    if (!FRAG_PERFIL.isAdded())
                        fragmentTransaction.add(R.id.content, FRAG_PERFIL); // Hay que hacerlo aquí
                                                                        // tambíen para que no pete.

                    fragmentTransaction.show(FRAG_PERFIL).commit();
                    seccion = R.id.navigation_perfil;

                    return true;
                case R.id.navigation_chat:

                    fragmentTransaction.show(FRAG_CHAT).commit();
                    seccion = R.id.navigation_chat;

                    return true;
            }

            return false;
        }

    };

    private void hacerScroll() {
        try {
            ((RecyclerView)FRAG_NOTICIA.getActivity().findViewById(R.id.recyclerView)).smoothScrollToPosition(0);
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
