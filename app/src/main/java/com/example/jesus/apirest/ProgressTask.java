package com.example.jesus.apirest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ProgressTask extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
    private String str_url_rss;
    private XmlPullParserFactory parseCreator;
    private ArrayList<String> datos; // Description, investigar como sacar datos de dentro.
    private ArrayList<String> titulo; // Titulo de la entrada.
    private ArrayList<String> imagen; // URL completa de la img -> http://www.vamosacorrer.com/imagenes/2017/04...ion.jpg
    private ArrayList<String> localizacion; // Eso.
    private ArrayList<String> fecha;

    public ProgressTask(Context context, String str_url_rss) {
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.str_url_rss = str_url_rss;
        this.datos = new ArrayList<>();

        this.titulo = new ArrayList<>();
        this.imagen = new ArrayList<>(); // De momento solo la URL, habría ademas que anadirle la parte fija : "vamosacorrer.com".
        this.localizacion = new ArrayList<>();
        this.fecha = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Leyendo Feeds RSS de " + this.str_url_rss);
        this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(final String... args) {

        try {
            URL url = new URL(str_url_rss);
            InputStream in = url.openStream();

            if (in != null) {
                parseCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parseCreator.newPullParser();
                parser.setInput(in, null);
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            //Títulos
                            if (tag.equalsIgnoreCase("title")) {
                                String title = parser.nextText();
                                titulo.add(title);
                            }
                            if (tag.equalsIgnoreCase("description")) {
                                String desc = parser.nextText();
                                //desc.replace("&aacute;", "á").replace("&eacute;", "é").replace("&oacute;", "ó"); //No funciona desde aquí

                                StringTokenizer st=new StringTokenizer(desc, "<");
                                while(st.hasMoreTokens()){
                                    String token=st.nextToken();
                                    //Imágenes
                                    if(token.contains("img")){
                                        StringTokenizer st2=new StringTokenizer(token, "\"");
                                        while (st2.hasMoreElements()){
                                            String token2=st2.nextToken();
                                            if(token2.contains("/imagenes")){
                                                imagen.add("http://www.vamosacorrer.com"+token2);
                                                break;
                                            }
                                        }
                                    }
                                    //Localizaciones
                                    //...
                                }
                            }
                            if(tag.equalsIgnoreCase("pubDate")){
                                String sFecha = parser.nextText();
                                if(sFecha.length()<20)
                                    fecha.add(sFecha);
                            }

                            break;
                    } // fin switch
                    parserEvent = parser.next();
                }
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (success) {
            Toast.makeText(context, "Feeds leidos", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context.getApplicationContext(), ActivityListTitle.class);
            i.putStringArrayListExtra("datos", datos); // PutExtra de description.
            i.putStringArrayListExtra("imagenes", imagen);
            i.putStringArrayListExtra("titulos", titulo);
            i.putStringArrayListExtra("fechas", fecha);
            i.putStringArrayListExtra("localizaciones", localizacion);
            context.startActivity(i);
        } else {
            Toast.makeText(context, "Error en la lectura", Toast.LENGTH_SHORT).show();
        }
    }
}
