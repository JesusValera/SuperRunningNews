package com.proyecto.tfg.superrunningnews.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.tfg.superrunningnews.helpers.ImageHelper;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignupActivity extends AppCompatActivity {

    private static final int SELECT_FILE = 1;
    private EditText etUsuario, etPassword;
    private Button btCrear, btImagen;
    private TextView tvLogin;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private Bitmap imagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        loadViews();
        createListeners();
        firebase();
        picture();
    }

    private void loadViews() {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btCrear = (Button) findViewById(R.id.btCrear);
        btImagen = (Button) findViewById(R.id.btImagen);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
    }

    private void createListeners() {
        btCrear.setOnClickListener(btCrear_OnClickListener);
        tvLogin.setOnClickListener(tvLogin_OnClickListener);
        btImagen.setOnClickListener(btImagen_OnClickListener);
    }

    private void firebase() {
        ref = FirebaseDatabase.getInstance().getReference("usuarios");
        storage = FirebaseStorage.getInstance();
    }

    private void picture() {
        imagenPerfil = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    //Listeners
    private View.OnClickListener btCrear_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String email = etUsuario.getText().toString();
            final String password = etPassword.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                                return;
                            }

                            //Guardar la imagen y después al usuario
                            StorageReference stref = storage.getReferenceFromUrl("gs://superrunningnews-75380.appspot.com/").child("imagenes/" + email + ".png");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imagenPerfil.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] data = baos.toByteArray();

                            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Creando cuenta...");

                            UploadTask uploadTask = stref.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(SignupActivity.this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String urlImg = taskSnapshot.getDownloadUrl().toString().substring(0, taskSnapshot.getDownloadUrl().toString().indexOf("&"));
                                    String nombre = email.replace(".", "").replace("#", "").replace("[", "").replace("]", "");
                                    ref.child(nombre).setValue(new Usuario(email, email, urlImg, password));

                                    btCrear.setEnabled(true);

                                    progressDialog.dismiss();

                                    Toast.makeText(SignupActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();

                                    //Iniciar la LoginActivity con el result
                                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                    i.putExtra("usuario", nombre);
                                    setResult(RESULT_OK, i);
                                    finish();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.show();
                                }
                            });
                        }
                    });
        }
    };

    private View.OnClickListener tvLogin_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
    };

    private View.OnClickListener btImagen_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccione una foto de perfil"), SELECT_FILE);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    int nh = (int) (bmp.getHeight() * (512.0 / bmp.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);

                    // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                    ImageView mImg = (ImageView) findViewById(R.id.ivImagen);
                    Bitmap imagen = ImageHelper.getCircularBitmap(scaled);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagen.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    mImg.setImageBitmap(imagen);
                    imagenPerfil = imagen;
                }
                break;
        }
    }

}