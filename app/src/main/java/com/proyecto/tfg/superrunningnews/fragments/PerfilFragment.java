package com.proyecto.tfg.superrunningnews.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.proyecto.tfg.superrunningnews.ImageHelper;
import com.proyecto.tfg.superrunningnews.LoginActivity;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.SplashActivity;
import com.proyecto.tfg.superrunningnews.models.Usuario;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PerfilFragment extends Fragment {

    private View view;

    private EditText etPassword, etPassword2;
    private ImageView ivImagen, ivPerfil;
    private Button btImagen, btUpdate, btBorrar, btLogout;
    private TextView tvUsuario;

    private FirebaseDatabase db;
    private DatabaseReference refurl;
    private FirebaseStorage storage;
    private HashMap<String, String> urls;

    private Bitmap imagenPerfil;
    private String usuario;

    private final int SELECT_FILE = 1;

    private boolean imagenEscogida = false;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Enganchar controles
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPassword2 = (EditText) view.findViewById(R.id.etPassword2);
        ivImagen = (ImageView) view.findViewById(R.id.ivImagen);
        ivPerfil = (ImageView) view.findViewById(R.id.ivPerfil);
        btImagen = (Button) view.findViewById(R.id.btImagen);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);
        btBorrar = (Button) view.findViewById(R.id.btBorrar);
        btLogout = (Button) view.findViewById(R.id.btLogout);
        tvUsuario = (TextView) view.findViewById(R.id.tvUsuario);


        //Con esto evitamos tener que pasar el usuario por otras vías más tediosas y largas
        //Si el usuario es nulo -> ver ponerImagen()
        usuario = SplashActivity.pref.getString("usuario", null);

        tvUsuario.setText(usuario);

        //Listeners
        btBorrar.setOnClickListener(btBorrar_OnClickListener);
        btImagen.setOnClickListener(btImagen_OnClickListener);
        btUpdate.setOnClickListener(btUpdate_OnClickListener);
        btLogout.setOnClickListener(btLogout_OnClickListener);

        //Firebase
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        urls = new HashMap<>();
        refurl = db.getReference("imagenes");

        refurl.addValueEventListener(refurl_ValueEventListener);

        Picasso.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/superrunningnews" +
                "-75380.appspot.com/o/imagenes%2F" + usuario + ".png?alt=media").into(ivImagen);
        Picasso.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/superrunningnews" +
                "-75380.appspot.com/o/imagenes%2F" + usuario + ".png?alt=media").into(ivPerfil);

        return view;
    }

    private void ponerImagen() {
        if (urls.get(usuario) != null) {
            StorageReference dref = storage.getReferenceFromUrl(urls.get(usuario));
            final long ONE_MEGABYTE = 1024 * 1024;
            dref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivImagen.setImageBitmap(bitmap);
                    ivPerfil.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

    }

    //Listeners Firebase
    private ValueEventListener refurl_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            urls.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                String url = data.getValue(String.class);
                urls.put(data.getKey(), url);
            }

            ponerImagen();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //Toast.makeText(PerfilFragment.this, "Error en la base de datos!", Toast.LENGTH_SHORT).show();
        }
    };

    //Listeners
    private View.OnClickListener btImagen_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccione una foto de perfil"), SELECT_FILE);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    int nh = (int) (bmp.getHeight() * (512.0 / bmp.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);

                    // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                    ImageView mImg = (ImageView) view.findViewById(R.id.ivImagen);
                    Bitmap imagen = ImageHelper.getCircularBitmap(scaled);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagen.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    mImg.setImageBitmap(imagen);
                    imagenPerfil = imagen;
                    imagenEscogida = true;
                }
                break;
        }
    }

    private View.OnClickListener btBorrar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dark_Dialog);
            builder.setTitle("Borrar perfil");
            builder.setMessage("¿Está seguro de que desea continuar?");
            builder.setCancelable(false);

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refurl.child(usuario).removeValue();
                    db.getReference("localizaciones").child(usuario).removeValue();
                    db.getReference("usuarios").child(usuario).removeValue();
                    StorageReference dref = storage.getReferenceFromUrl(urls.get(usuario));
                    dref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Perfil borrado correctamente!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al  borrar perfil!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            });
            builder.show();

        }
    };

    private View.OnClickListener btUpdate_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!etPassword.getText().toString().equals("")) {
                if (!validar()) {
                    return;
                }
                db.getReference("usuarios").child(usuario).setValue(new Usuario(usuario, usuario,
                        "https://firebasestorage.googleapis.com/v0/b/superrunningnews-75380.appspot.com/o/imagenes%2F" +
                                usuario + ".png?alt=media", true, etPassword.getText().toString()));
            }

            if (imagenEscogida) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Actualizando cuenta...");
                StorageReference stref = storage.getReferenceFromUrl("gs://superrunningnews-75380.appspot.com/").child("imagenes/" + usuario + ".png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagenPerfil.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = stref.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        db.getReference("imagenes").child(usuario).setValue(taskSnapshot.getDownloadUrl().toString());
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.show();
                    }
                });
            }

            Toast.makeText(getContext(), "Usuario actualizado correctamente!", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener btLogout_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
        }
    };

    private boolean validar() {
        boolean valido = true;

        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            etPassword.setError("entre 4 y 12 caracteres alfanuméricos");
            valido = false;
        } else {
            etPassword.setError(null);
        }

        if (password2.isEmpty() || password2.length() < 4 || password2.length() > 12) {
            etPassword2.setError("entre 4 y 12 caracteres alfanuméricos");
            valido = false;
        } else {
            etPassword2.setError(null);
        }

        if (!password.equals(password2)) {
            etPassword.setError("las contraseñas deben de ser iguales");
            valido = false;
        } else {
            etPassword2.setError(null);
        }

        return valido;
    }

}