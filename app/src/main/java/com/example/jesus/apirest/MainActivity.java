package com.example.jesus.apirest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static ArrayList<String> list_titles;
    public static ArrayList<String> list_content;
    private Button boton_leer;
    private EditText et_rss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.boton_leer = (Button) findViewById(R.id.button1);
        this.et_rss = (EditText) findViewById(R.id.editText1);

        list_titles = new ArrayList<>();
        list_content = new ArrayList<>();

        this.boton_leer.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.button1) :
                AsyncTask task = new ProgressTask(this, et_rss.getText().toString()).execute();
                break;
        }

    }
}
