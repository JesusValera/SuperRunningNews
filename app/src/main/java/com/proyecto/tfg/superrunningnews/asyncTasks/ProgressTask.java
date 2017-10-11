package com.proyecto.tfg.superrunningnews.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.tfg.superrunningnews.BottomBarActivity;
import com.proyecto.tfg.superrunningnews.activities.LoginActivity;
import com.proyecto.tfg.superrunningnews.activities.SplashActivity;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.proyecto.tfg.superrunningnews.models.Favorito;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Usuario;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ProgressTask extends AsyncTask<String, Void, Boolean> {

    private final String urlRss = "http://www.vamosacorrer.com/rss/feeds/rss_carreras.xml";
    private ProgressDialog dialog;
    private Context context;
    private ArrayList<String> titulo;
    private ArrayList<String> imagen; // URL img -> http://www.vamosacorrer.com/imagenes/2017/04...ion.jpg
    private ArrayList<String> localizacion;
    private ArrayList<String> fecha;
    private ArrayList<String> link;
    private ArrayList<Noticia> tNoticia;
    private boolean primera = false;
    private int caller;
    private FirebaseDatabase db;
    private ArrayList<Favorito> tFavoritos;
    private ArrayList<Dialog> tGruposNuevos;
    private ArrayList<Dialog> tGruposOriginales;
    private ArrayList<Dialog> tGruposOriginalesAux; // Creo esta auxiliar porque cuando hago
                                                    // removeAll, la lista 'tGruposOriginales'
                                                    // se modifica.

    private final String NOTICIA_EXTRA_TITULO = "Carrera de los Caballos del Vino";
    private final String NOTICIA_EXTRA_LOCALIZACION = "Caravaca de la Cruz";
    private final String NOTICIA_EXTRA_LINK = "http://www.caravaca.org/";
    private final String NOTICIA_EXTRA_IMAGEN = "https://firebasestorage.googleapis.com/v0/b/superrunningnews-75380.appspot.com/o/imagenes%2Fcaravaca_castillo.jpg?alt=media&token=4cf84dc4-6a8d-4498-bdaa-7f1f7876fd1f";
    private final double NOTICIA_EXTRA_LAT = 38.1055518;
    private final double NOTICIA_EXTRA_LNG = -1.8662166;
    private final int ONE_DAY_MORE_FROM_TODAY = 1000 * 24 * 60 * 60;

    public ProgressTask(Context context, int caller) {
        this.context = context;
        this.dialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        this.caller = caller;
        this.titulo = new ArrayList<>();
        this.imagen = new ArrayList<>();
        this.localizacion = new ArrayList<>();
        this.fecha = new ArrayList<>();
        this.link = new ArrayList<>();
        this.tNoticia = new ArrayList<>();
        this.tFavoritos = new ArrayList<>();
        this.tGruposOriginales = new ArrayList<>();
        this.tGruposOriginalesAux = new ArrayList<>();
        this.tGruposNuevos = new ArrayList<>();
        String usuario = SplashActivity.pref.getString("usuario", null);
        db = FirebaseDatabase.getInstance();
        DatabaseReference refFav = db.getReference("favoritos/" + usuario);
        refFav.addValueEventListener(refFav_ValueEventListener);

        DatabaseReference refGrupo = db.getReference("grupos/");
        refGrupo.addListenerForSingleValueEvent(refGrupo_ValueEventListener);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Cargando Feed de Noticias...");
        this.dialog.setCancelable(false);

        if (caller == LoginActivity.CALLER_LOGIN) {
            try {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.up);
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SystemClock.sleep(500);
            this.dialog.show();
        }
    }

    @Override
    protected Boolean doInBackground(final String... args) {
        try {
            URL url = new URL(urlRss);
            InputStream is = url.openStream();

            if (is != null) {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(is, null);
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            titulo(parser, tag);
                            descripcion(parser, tag);
                            fecha(parser, tag);
                            link(parser, tag);

                            break;
                    }
                    parserEvent = parser.next();

                    mostrarBarraProgreso();
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void titulo(XmlPullParser parser, String tag) throws Exception {
        if (tag.equalsIgnoreCase("title")) {
            String title = parser.nextText();
            if (!title.equals("RSS de Carreras de vamosacorrer.com")) {
                // Titulo no puede contener ".", "#", "[", "]".
                String tituloFormateado = title.replace(".", "")
                        .replace("#", "")
                        .replace("[", "")
                        .replace("]", "");
                titulo.add(tituloFormateado);
            }
        }
    }

    private void descripcion(XmlPullParser parser, String tag) throws Exception {
        if (tag.equalsIgnoreCase("description")) {
            if (!primera) {
                if (!imagen(parser)) {
                    imagen.add("http://orig12.deviantart.net/96a5/f/2017/101/7/0/img_default_by_hollowkrator-db5f21z.png");
                }
            } else {
                parser.nextText();
                primera = true;
            }
        }
    }

    private boolean imagen(XmlPullParser parser) throws Exception {
        String desc = parser.nextText();
        StringTokenizer st = new StringTokenizer(desc, "<");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.contains("img")) {
                StringTokenizer st2 = new StringTokenizer(token, "\"");
                while (st2.hasMoreElements()) {
                    String token2 = st2.nextToken();
                    if (token2.contains("/imagenes")) {
                        imagen.add("http://www.vamosacorrer.com" + token2);
                        return true;
                    }
                }
                break;
            }
        }

        return false;
    }

    private void fecha(XmlPullParser parser, String tag) throws Exception {
        if (tag.equalsIgnoreCase("pubDate")) {
            String sFecha = parser.nextText();
            if (sFecha.length() < 20) {
                try {
                    Locale locale = new Locale("ES", "es");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", locale);
                    String sFecha2 = sdf.format(sdf2.parse(sFecha.substring(0, 10)));
                    fecha.add(sFecha2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void link(XmlPullParser parser, String tag) throws Exception {
        if (tag.equalsIgnoreCase("link")) {
            String sLink = parser.nextText();
            if (!sLink.equals("http://www.vamosacorrer.com")) {
                link.add(sLink);
                localizacion(sLink);
            }
        }
    }

    private void localizacion(String sLink) throws IOException {
        Document doc = Jsoup.connect(sLink).get();
        Elements loc = doc.select("dd[itemprop=location]");
        String location = loc.text();
        StringTokenizer st = new StringTokenizer(location, "-");
        localizacion.add(st.nextToken().trim());
    }

    private void mostrarBarraProgreso() {
        if (localizacion.size() == 20 && caller == SplashActivity.CALLER_SPLASH) {
            publishProgress();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            crearNoticias();
            addNoticiaExtra();
            actualizarGrupos();
            ejecutarActividad();
        } else {
            Toast.makeText(context,
                    R.string.error_lectura_internet,
                    Toast.LENGTH_SHORT).show();
            ((SplashActivity)context).finish();
        }

        ocultarDialogoCarga();
    }

    private void crearNoticias() {
        Geocoder geocoder = new Geocoder(context, new Locale("es", "ES"));
        for (int i = 0; i < fecha.size(); i++) {
            Noticia noticia = new Noticia();
            noticia.setTitulo(titulo.get(i));
            noticia.setLocalizacion(localizacion.get(i));
            noticia.setLink(link.get(i));
            noticia.setFecha(fecha.get(i));
            noticia.setImagen(imagen.get(i + 1));
            noticia.setLatLng(obtenerLatLng(i, geocoder));
            noticia.setFavorito(getFavorito(i));

            tNoticia.add(noticia);
            tGruposNuevos.add(crearGrupo(noticia));
        }
    }

    private LatLng obtenerLatLng(int i, Geocoder geocoder) {
        try {
            List<Address> pos = geocoder.getFromLocationName(localizacion.get(i), 1);
            if (pos.size() != 0) {
                return new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude());
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }

    private boolean getFavorito(int i) {
        for (Favorito fav : tFavoritos) {
            if (fav.getNombreEvento().equals(titulo.get(i))) {
                return true;
            }
        }

        return false;
    }

    private void addNoticiaExtra() {
        Noticia noticia = crearNoticiaExtra();
        tNoticia.add(noticia);
        tGruposNuevos.add(crearGrupo(noticia));
    }

    private Noticia crearNoticiaExtra() {
        Noticia noticia = new Noticia();
        noticia.setTitulo(NOTICIA_EXTRA_TITULO);
        noticia.setLocalizacion(NOTICIA_EXTRA_LOCALIZACION);
        noticia.setLink(NOTICIA_EXTRA_LINK);
        noticia.setFecha(generarFecha("dd/MM/yyyy", new Locale("ES", "es")));
        noticia.setImagen(NOTICIA_EXTRA_IMAGEN);
        noticia.setLatLng(new LatLng(NOTICIA_EXTRA_LAT, NOTICIA_EXTRA_LNG));
        return noticia;
    }

    private String generarFecha(String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(new Date().getTime() + ONE_DAY_MORE_FROM_TODAY);
    }

    private Dialog crearGrupo(Noticia noticia) {
        return new Dialog(
                noticia.getTitulo(),
                noticia.getTitulo(),
                noticia.getImagen(),
                new ArrayList<Usuario>()
        );
    }

    private void actualizarGrupos() {
        // Borramos los que ya no existan (eventos que ya han sido eliminados).
        tGruposOriginales.removeAll(tGruposNuevos); // Noticias que ya no existen.
        for (int i = 0; i < tGruposOriginales.size(); i++) {
            db.getReference("grupos/").child(tGruposOriginales.get(i).getDialogName()).removeValue();
        }

        // Y creamos los que no esten.
        tGruposNuevos.removeAll(tGruposOriginalesAux);
        for (int i = 0; i < tGruposNuevos.size(); i++) {
            db.getReference("grupos/").child(tGruposNuevos.get(i).getDialogName()).setValue(tGruposNuevos.get(i));
        }

        Collections.sort(tNoticia);
    }

    private void ejecutarActividad() {
        Intent i = new Intent(context.getApplicationContext(), BottomBarActivity.class);
        i.putParcelableArrayListExtra("noticia", tNoticia);
        context.startActivity(i);
    }

    private void ocultarDialogoCarga() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //Listener Firebase
    private ValueEventListener refFav_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            tFavoritos.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Favorito favorito = data.getValue(Favorito.class);
                tFavoritos.add(favorito);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(context, "(FV) Error en la base de datos!", Toast.LENGTH_SHORT).show();
        }
    };

    private ValueEventListener refGrupo_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Dialog dialog = data.getValue(Dialog.class);
                tGruposOriginales.add(dialog);
                tGruposOriginalesAux.add(dialog);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(context, "(GP) Error en la base de datos!", Toast.LENGTH_SHORT).show();
        }
    };

}
