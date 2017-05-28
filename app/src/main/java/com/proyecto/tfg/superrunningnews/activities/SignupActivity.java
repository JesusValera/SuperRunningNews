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

import com.proyecto.tfg.superrunningnews.helpers.ImageHelper;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btCrear, btImagen;
    private TextView tvLogin;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseStorage storage;

    private ArrayList<Usuario> usuarios;

    private static final int SELECT_FILE = 1;
    private Bitmap imagenPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Enganchar controles
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btCrear = (Button) findViewById(R.id.btCrear);
        btImagen = (Button) findViewById(R.id.btImagen);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        //Listeners
        btCrear.setOnClickListener(btCrear_OnClickListener);
        tvLogin.setOnClickListener(tvLogin_OnClickListener);
        btImagen.setOnClickListener(btImagen_OnClickListener);

        //Firebase
        usuarios = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("usuarios");
        storage = FirebaseStorage.getInstance();


        ref.addValueEventListener(ref_ValueEventListener);

        //Imagen
        imagenPerfil = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

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
            Toast.makeText(SignupActivity.this, "Error en la base de datos!", Toast.LENGTH_SHORT).show();
        }
    };


    //Listeners
    private View.OnClickListener btCrear_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validar()) {
                Toast.makeText(SignupActivity.this, "No se ha podido crear la cuenta!", Toast.LENGTH_SHORT).show();
                btCrear.setEnabled(true);
                return;
            }

            btCrear.setEnabled(false);

            final String usuario = etUsuario.getText().toString();
            final String password = etPassword.getText().toString();

            //Crear los datos en Firebase
            boolean existe = false;
            for (Usuario user : usuarios) {
                if (user.getName().equals(usuario)) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                etUsuario.setError("El usuario ya existe");
                btCrear.setEnabled(true);
                etUsuario.requestFocus();
                return;
            }

            //Guardar la imagen y después al usuario
            StorageReference stref = storage.getReferenceFromUrl("gs://superrunningnews-75380.appspot.com/").child("imagenes/" + usuario + ".png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //Bitmap imagen= ImageHelper.getCircularBitmap(imagenPerfil);
            imagenPerfil.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            //ByteArrayInputStream bs = new ByteArrayInputStream(data);

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
                    return;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String urlImg = taskSnapshot.getDownloadUrl().toString().substring(0, taskSnapshot.getDownloadUrl().toString().indexOf("&"));
                    ref.child(usuario).setValue(new Usuario(usuario, usuario, urlImg, password));
                    //db.getReference("usuarios").child(usuario).child("avatar").setValue();

                    btCrear.setEnabled(true);

                    progressDialog.dismiss();

                    Toast.makeText(SignupActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();

                    //Iniciar la LoginActivity con el result
                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    i.putExtra("usuario", usuario);
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

        private boolean validar() {
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

    };

    private View.OnClickListener tvLogin_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
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