package com.proyecto.tfg.superrunningnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.asyncTasks.ProgressTask;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btLogin;
    private CheckBox cbRecordar;
    private TextView tvSingup;

    public static final int SINGUP = 1;

    private ArrayList<Usuario> usuarios;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public static final int CALLER_LOGIN = 2;

    //private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //pref = PreferenceManager.getDefaultSharedPreferences(SplashActivity.splashContext);

        //Enganchar controles
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvSingup = (TextView) findViewById(R.id.tvSingup);
        cbRecordar = (CheckBox) findViewById(R.id.cbRecordar);

        //Listeners
        btLogin.setOnClickListener(btLogin_OnClickListener);
        tvSingup.setOnClickListener(tvSingup_OnClickListener);

        //Firebase
        usuarios = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("usuarios");

        ref.addValueEventListener(ref_ValueEventListener);

        // Check Permissions
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 0);
        }

        if (ActivityCompat.checkSelfPermission(this, "android.permission.INTERNET") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.INTERNET"}, 0);
        }

    }

    //Listener Firebase
    private ValueEventListener ref_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            usuarios.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Usuario user = data.getValue(Usuario.class);
                usuarios.add(user);
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
                //Toast.makeText(LoginActivity.this, "No se ha podido conectar!", Toast.LENGTH_SHORT).show();
                //btLogin.setEnabled(true);
                return;
            }

            final String usuario = etUsuario.getText().toString();
            String password = etPassword.getText().toString();

            //Comprobar los datos en Firebase
            boolean existe = false;
            boolean passvalid = false;
            for (Usuario user : usuarios) {
                if (user.getNombre().equals(usuario)) {
                    existe = true;
                    if (user.getPassword().equals(password)) {
                        passvalid = true;
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
            if (!passvalid) {
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

        String usuario = etUsuario.getText().toString();
        String password = etPassword.getText().toString();

        if (usuario.isEmpty() || usuario.length() < 3 || usuario.length() > 20) {
            etUsuario.setError("entre 3 y 20 caracteres");
            valido = false;
        } else {
            etUsuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
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

    /*@Override
    public void onBackPressed() {
        moveTaskToBack(true);
        //super.onBackPressed();
    }*/

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
