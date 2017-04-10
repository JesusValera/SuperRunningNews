package com.example.jesus.apirest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityListTitle extends Activity {

    private ArrayList<String> titulos;
    private ArrayList<String> imagenes;
    private ArrayList<String> localizaciones;
    private ArrayList<String> fechas;
    private ArrayList<String> links;

    private ArrayList<Noticia> noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_title);

        Intent i = getIntent();

        imagenes = i.getExtras().getStringArrayList("imagenes");
        titulos = i.getExtras().getStringArrayList("titulos");
        fechas = i.getExtras().getStringArrayList("fechas");
        localizaciones = i.getExtras().getStringArrayList("localizaciones");
        links = i.getExtras().getStringArrayList("links");

        noticias = new ArrayList<>();

        for (int j = 0; j < 30; j++) {
            Noticia n=new Noticia(titulos.get(j),
                                  imagenes.get(j),
                                  localizaciones.get(j),
                                  fechas.get(j),
                                  links.get(j));
            noticias.add(n);
        }

        ArrayAdapter<Noticia> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noticias);
        ListView list_titles = (ListView) findViewById(R.id.listView1);
        list_titles.setAdapter(adapter);

    }

}
