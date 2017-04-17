package com.proyecto.tfg.superrunningnews;

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

        // Pasar objeto Usuario para contruir FRAG_PERFIL (¿y Chat?).
        /*Bundle bundleUsuario = new Bundle();
        bundleUsuario.putParcelable("usuario", usuario); //--> ¡¡Usuario no es parcelable, aún!!
        FRAG_PERFIL.setArguments(bundleUsuario);
        FRAG_CHAT.setArguments(bundleUsuario); //(??)*/

        Bundle bundleNoticia = new Bundle();
        bundleNoticia.putParcelableArrayList("noticia", tNoticias);
        FRAG_NOTICIA.setArguments(bundleNoticia);
        FRAG_MAPA.setArguments(bundleNoticia);

        // TODO -> Dialogo cargar mapa al principio o cuando se pulse sobre el botón "Mapa"¿?
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, FRAG_NOTICIA)
                /*.add(R.id.content, FRAG_MAPA)*/
                .add(R.id.content, FRAG_PERFIL)
                .add(R.id.content, FRAG_CHAT).commit();
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

                    return true;
                case R.id.navigation_mapa:

                    if (!FRAG_MAPA.isAdded())
                        fragmentTransaction.add(R.id.content, FRAG_MAPA); // <-- !!

                    fragmentTransaction.show(FRAG_MAPA).commit();
                    seccion = R.id.navigation_mapa;

                    return true;
                case R.id.navigation_perfil:

                    fragmentTransaction.show(FRAG_PERFIL).commit();
                    seccion = R.id.navigation_perfil;

                    return true;

                case R.id.navigation_chat:
                    // Si es mucho trabajo, comentar esto y ocultar boton Chat...
                    fragmentTransaction.show(FRAG_CHAT).commit();
                    seccion = R.id.navigation_chat;

                    return true;
            }

            return false;
        }

    };

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
            Toast.makeText(getBaseContext(), "Presione una vez más para salir.", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

}
