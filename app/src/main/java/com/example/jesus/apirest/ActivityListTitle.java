package com.example.jesus.apirest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityListTitle extends Activity {

    private String[] titles_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_title);
        titles_str = new String[MainActivity.list_content.size()];
        int i = 0;
        for (String t: MainActivity.list_content) {
            titles_str[i] = t;
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.titles_str);
        ListView list_titles = (ListView) findViewById(R.id.listView1);
        list_titles.setAdapter(adapter);

    }





}
