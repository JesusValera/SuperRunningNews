package com.example.jesus.apirest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityListTitle extends Activity {

    private ArrayList<String> content_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_title);

        Intent i = getIntent();
        content_str =  i.getExtras().getStringArrayList("datos"); // Obtener arraylist desde el intent.

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, content_str);
        ListView list_titles = (ListView) findViewById(R.id.listView1);
        list_titles.setAdapter(adapter);

    }





}
