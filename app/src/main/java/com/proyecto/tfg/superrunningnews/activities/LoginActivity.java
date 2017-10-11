package com.proyecto.tfg.superrunningnews.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.asyncTasks.ProgressTask;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static final int SINGUP = 1;
    public static final int CALLER_LOGIN = 2;
    private EditText etUsuario, etPassword;
    private Button btLogin;
    private CheckBox cbRecordar;
    private TextView tvSingup;
    private ArrayList<Usuario> users;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        users = new ArrayList<>();
        loadViews();
        listeners();
        firebase();
    }

    private void loadViews() {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvSingup = (TextView) findViewById(R.id.tvSingup);
        cbRecordar = (CheckBox) findViewById(R.id.cbRecordar);
    }

    private void listeners() {
        btLogin.setOnClickListener(btLogin_OnClickListener);
        tvSingup.setOnClickListener(tvSingup_OnClickListener);
    }

    private void firebase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");
        ref.addValueEventListener(ref_ValueEventListener);
    }

    //Listener Firebase
    private ValueEventListener ref_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            users.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Usuario user = data.getValue(Usuario.class);
                users.add(user);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(LoginActivity.this, "Error en la base de datos!", Toast.LENGTH_SHORT).show();
        }
    };

    //Listeners
    private View.OnClickListener btLogin_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validar()) {
                return;
            }

            String userName = etUsuario.getText().toString();
            String userPassword = etPassword.getText().toString();

            //Comprobar los datos en Firebase
            boolean existe = false;
            boolean validPassword = false;
            for (Usuario user : users) {
                if (user.getName().equals(userName)) {
                    existe = true;
                    if (user.getPassword().equals(userPassword)) {
                        validPassword = true;
                    }
                    break;
                }
            }
            if (!existe) {
                etUsuario.setError("El usuario no existe");
                btLogin.setEnabled(true);
                etUsuario.requestFocus();
                return;
            }
            if (!validPassword) {
                etPassword.setError("Contraseña incorrecta");
                btLogin.setEnabled(true);
                etPassword.requestFocus();
                return;
            }

            SharedPreferences.Editor editor = SplashActivity.pref.edit();
            if (cbRecordar.isChecked()) editor.putBoolean("login", true).apply();
            else editor.putBoolean("login", false).apply();

            editor.putString("usuario", etUsuario.getText().toString()).apply();

            new ProgressTask(LoginActivity.this, CALLER_LOGIN).execute();
        }
    };

    private View.OnClickListener tvSingup_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(i, SINGUP);
        }
    };

    public boolean validar() {
        boolean valido = true;

        String userName = etUsuario.getText().toString();
        String userPassword = etPassword.getText().toString();

        if (userName.isEmpty() || userName.length() < 3 || userName.length() > 20) {
            etUsuario.setError("entre 3 y 20 caracteres");
            valido = false;
        } else {
            etUsuario.setError(null);
        }

        if (userPassword.isEmpty() || userPassword.length() < 4 || userPassword.length() > 12) {
            etPassword.setError("entre 4 y 12 caracteres alfanuméricos");
            valido = false;
        } else {
            etPassword.setError(null);
        }

        return valido;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SINGUP) {
            if (resultCode == RESULT_OK) {
                etUsuario.setText(data.getStringExtra("usuario"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        int TIME_INTERVAL_IN_MILLIS = 2000;
        if (mBackPressed + TIME_INTERVAL_IN_MILLIS > System.currentTimeMillis()) {
            moveTaskToBack(true);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Presione una vez más para salir.", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
