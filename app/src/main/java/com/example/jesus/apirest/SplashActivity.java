package com.example.jesus.apirest;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class SplashActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash);

        //Si ya estaba logueado, ejecutar el ProgressTask, si no la LoginActivity
        SystemClock.sleep(2000);
        new ProgressTask(this).execute();
    }






}
