package com.proyecto.tfg.superrunningnews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
import com.proyecto.tfg.superrunningnews.services.NotificacionesService;

import java.util.ArrayList;
import java.util.Calendar;

public class BottomBarActivity extends AppCompatActivity {

    public static ArrayList<Noticia> noticias;
    private final MapaFragment FRAG_MAPA = new MapaFragment();
    private final NoticiaFragment FRAG_NOTICIA = new NoticiaFragment();
    private final PerfilFragment FRAG_PERFIL = new PerfilFragment();
    private final ChatFragment FRAG_CHAT = new ChatFragment();
    private final String CURRENT_SECTION = "currentSection";
    private static final int TIME_INTERVAL = 2000; // # milliseconds between two back presses.
    private ArrayList<Noticia> tNoticias;
    private int seccion;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        tNoticias = getIntent().getParcelableArrayListExtra("noticia");
        noticias = tNoticias;

        addDataToFragments();
        getCurrentSection(savedInstanceState);
        PlayMusicBeginning();
        startService();
    }

    private void addDataToFragments() {
        Bundle bundleNoticia = new Bundle();
        bundleNoticia.putParcelableArrayList("noticia", tNoticias);
        FRAG_NOTICIA.setArguments(bundleNoticia);
        FRAG_MAPA.setArguments(bundleNoticia);
    }

    private void getCurrentSection(Bundle savedInstanceState) {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            seccion = savedInstanceState.getInt(CURRENT_SECTION);
            navigation.setSelectedItemId(seccion);
        } else {
            seccion = R.id.navigation_noticias;
            navigation.setSelectedItemId(seccion);
        }
    }

    private void PlayMusicBeginning() {
        try {
            MediaPlayer.create(getApplicationContext(), R.raw.coin).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startService() {
        Intent i = new Intent(BottomBarActivity.this, NotificacionesService.class);
        PendingIntent servicePendingIntent = PendingIntent.getService(BottomBarActivity.this, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, servicePendingIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_noticias:
                    cambiarPantalla(FRAG_NOTICIA, R.id.navigation_noticias);
                    scrollTo1stPos();
                    return true;

                case R.id.navigation_mapa:
                    cambiarPantalla(FRAG_MAPA, R.id.navigation_mapa);
                    return true;

                case R.id.navigation_perfil:
                    cambiarPantalla(FRAG_PERFIL, R.id.navigation_perfil);
                    return true;

                case R.id.navigation_chat:
                    cambiarPantalla(FRAG_CHAT, R.id.navigation_chat);
                    return true;
            }

            return false;
        }

    };

    private void cambiarPantalla(Fragment fragment, int seccion) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment).commit();
        this.seccion = seccion;
    }

    private void scrollTo1stPos() {
        try {
            ((RecyclerView) FRAG_NOTICIA.getActivity()
                    .findViewById(R.id.recyclerView))
                    .smoothScrollToPosition(0);
        } catch (NullPointerException e) { ; }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SECTION, seccion);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            moveTaskToBack(true);
            return;
        } else {
            Toast.makeText(getBaseContext(),
                    R.string.salir,
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

}
