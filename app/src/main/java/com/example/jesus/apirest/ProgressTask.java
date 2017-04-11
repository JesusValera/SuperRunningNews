package com.example.jesus.apirest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.StreamHandler;

public class ProgressTask extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
    private final String urlRss = "http://www.vamosacorrer.com/rss/feeds/rss_carreras.xml"; // Privado y final.
    private XmlPullParserFactory parseCreator;
    private ArrayList<String> titulo; // Titulo de la entrada.
    private ArrayList<String> imagen; // URL completa de la img -> http://www.vamosacorrer.com/imagenes/2017/04...ion.jpg
    private ArrayList<String> localizacion; // Eso.
    private ArrayList<String> fecha;
    private ArrayList<String> link;
    private ArrayList<Noticia> tNoticia;
    private boolean primera=false;

    //Crear variable a partir de los datos de las preferencias. Hacerlo para que cuando ya se esté logueado,
    //no salga el dialog y el splash screen sirva como pantalla de carga.

    public ProgressTask(Context context) {
        this.context = context;
        this.dialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);

        this.titulo = new ArrayList<>();
        this.imagen = new ArrayList<>();
        this.localizacion = new ArrayList<>();
        this.fecha = new ArrayList<>();
        this.link = new ArrayList<>();
        tNoticia = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Cargando Feed de Noticias...");
        this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(final String... args) {
        try {
            URL url = new URL(urlRss);
            InputStream is = url.openStream();

            if (is != null) {
                parseCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parseCreator.newPullParser();
                parser.setInput(is, null);
                int parserEvent = parser.getEventType();
                //Noticia noticia = new Noticia(); // No funciona asi. (?)

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            //Títulos
                            if (tag.equalsIgnoreCase("title")) {
                                String title = parser.nextText();
                                if(!title.equals("RSS de Carreras de vamosacorrer.com")) {
                                    titulo.add(title);
                                    //noticia.setTitulo(title);
                                }
                            }

                            if (tag.equalsIgnoreCase("description")) {
                                if (!primera){
                                    String desc = parser.nextText();

                                    boolean tieneImagen=false;
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
                                                    //noticia.setImagen("http://vamosacorrer.com" + token2);
                                                    tieneImagen=true;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if(!tieneImagen){
                                        imagen.add("http://orig12.deviantart.net/96a5/f/2017/101/7/0/img_default_by_hollowkrator-db5f21z.png");
                                    }
                                } else {
                                    parser.nextText();
                                    primera=true;
                                }

                            }

                            //Fecha
                            if(tag.equalsIgnoreCase("pubDate")){
                                String sFecha = parser.nextText();
                                if(sFecha.length()<20) {

                                    SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
                                    SimpleDateFormat sdf2 =new SimpleDateFormat("yyyy-MM-dd");
                                    try{
                                        String sFecha2=sdf.format(sdf2.parse(sFecha.substring(0, 10)));
                                        fecha.add(sFecha2);
                                    } catch (Exception e){}

                                    //noticia.setFecha(sFecha);
                                }
                            }

                            //Link
                            if(tag.equalsIgnoreCase("link")){
                                String sLink = parser.nextText();
                                if(!sLink.equals("http://www.vamosacorrer.com")){
                                    link.add(sLink);
                                    //noticia.setLink(sLink);

                                    //Localizaciones
                                    URL url2=new URL(sLink);
                                    Document doc = Jsoup.connect(sLink).get();
                                    Elements loc = doc.select("dd[itemprop=location]");
                                    String location=loc.text();
                                    StringTokenizer st=new StringTokenizer(location, "-");
                                    localizacion.add(st.nextToken().trim());
                                    //noticia.setLocalizacion(st.nextToken().trim());
                                }
                            }

                            break;
                    } // fin switch
                    parserEvent = parser.next();
                    //tNoticia.add(noticia);
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

            for (int i = 0; i < fecha.size(); i++) {
                Noticia noticia = new Noticia();
                noticia.setTitulo(titulo.get(i));
                noticia.setLocalizacion(localizacion.get(i));
                noticia.setLink(link.get(i));
                noticia.setFecha(fecha.get(i));
                noticia.setImagen(imagen.get(i+1));
                tNoticia.add(noticia);
            }

            //Toast.makeText(context, "Feeds leidos", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context.getApplicationContext(), BottomBarActivity.class);
//            i.putStringArrayListExtra("imagenes", imagen);
//            i.putStringArrayListExtra("titulos", titulo);
//            i.putStringArrayListExtra("fechas", fecha);
//            i.putStringArrayListExtra("localizaciones", localizacion);
//            i.putStringArrayListExtra("links", link);
            i.putParcelableArrayListExtra("noticia", tNoticia);
            context.startActivity(i);
        } else {
            Toast.makeText(context, "Error en la lectura", Toast.LENGTH_SHORT).show();

        }
    }
}
