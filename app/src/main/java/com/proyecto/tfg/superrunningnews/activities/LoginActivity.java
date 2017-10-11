package com.proyecto.tfg.superrunningnews.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.asyncTasks.ProgressTask;

public class LoginActivity extends AppCompatActivity {

    public static final int SINGUP = 1;
    public static final int CALLER_LOGIN = 2;
    private final String TAG = "TAG";
    private EditText etUsuario, etPassword;
    private Button btLogin;
    private CheckBox cbRecordar;
    private TextView tvSingup;
    private long mBackPressed;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loadViews();
        listeners();
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

    //Listeners
    private View.OnClickListener btLogin_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userName = etUsuario.getText().toString();
            String userPassword = etPassword.getText().toString();

            mAuth.signInWithEmailAndPassword(userName, userPassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                                return;
                            }

                            SharedPreferences.Editor editor = SplashActivity.pref.edit();
                            if (cbRecordar.isChecked()) editor.putBoolean("login", true).apply();
                            else editor.putBoolean("login", false).apply();

                            String nombre = etUsuario.getText().toString().replace(".", "").replace("#", "").replace("[", "").replace("]", "");
                            editor.putString("usuario", nombre).apply();

                            new ProgressTask(LoginActivity.this, CALLER_LOGIN).execute();
                        }
                    });
        }
    };

    private View.OnClickListener tvSingup_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(i, SINGUP);
        }
    };

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
