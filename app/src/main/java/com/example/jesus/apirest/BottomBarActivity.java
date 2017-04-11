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

//    private TextView mTextMessage;
//    private ArrayList<String> titulos;
//    private ArrayList<String> imagenes;
//    private ArrayList<String> localizaciones;
//    private ArrayList<String> fechas;
//    private ArrayList<String> links;
//
    private ArrayList<Noticia> noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_noticias);

        Intent i = getIntent();

        this.noticias = i.getParcelableArrayListExtra("noticia");
        Toast.makeText(getApplicationContext(), "Noticia N5: " + noticias.get(4).getTitulo(), Toast.LENGTH_SHORT).show(); // Prueba.

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_noticias:
                    //mTextMessage.setText(R.string.title_noticias);

                    FragmentFeed noticiaFragment = new FragmentFeed();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, noticiaFragment).commit();

                    return true;
                case R.id.navigation_mapa:
                    //mTextMessage.setText(R.string.title_mapa);

                    FragmentMapa mapaFragment = new FragmentMapa();
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
