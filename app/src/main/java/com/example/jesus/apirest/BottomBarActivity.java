package com.example.jesus.apirest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jesus.apirest.Fragments.FragmentMapa;
import com.example.jesus.apirest.Fragments.FragmentFeed;
import com.example.jesus.apirest.Fragments.FragmentPerfil;

import java.util.ArrayList;

public class BottomBarActivity extends AppCompatActivity {

    private ArrayList<Noticia> noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent i = getIntent();
        this.noticias = i.getParcelableArrayListExtra("noticia");

        // Cargar por defecto la pantalla de noticias.
        navigation.setSelectedItemId(R.id.navigation_noticias);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_noticias:

                    FragmentFeed noticiaFragment = new FragmentFeed();
                    Bundle bundleFeed = new Bundle();
                    bundleFeed.putParcelableArrayList("noticia", noticias);
                    noticiaFragment.setArguments(bundleFeed);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, noticiaFragment).commit();

                    return true;
                case R.id.navigation_mapa:

                    FragmentMapa mapaFragment = new FragmentMapa();

                    Bundle bundleMap = new Bundle();
                    bundleMap.putParcelableArrayList("noticia", noticias);
                    mapaFragment.setArguments(bundleMap);

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, mapaFragment).commit();

                    return true;
                case R.id.navigation_perfil:
                    //mTextMessage.setText(R.string.title_perfil);

                    FragmentPerfil perfilFragment = new FragmentPerfil();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, perfilFragment).commit();

                    return true;
            }
            return false;
        }

    };

}
