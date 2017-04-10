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

/**
 * Created by SusVa on 7/04/17.
 */

public class ProgressTask extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
    private String str_url_rss;

    public ProgressTask(Context context, String str_url_rss) {
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.str_url_rss = str_url_rss;
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Leyendo Feeds RSS de " + this.str_url_rss);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (success) {
            Toast.makeText(context, "Feeds leidos", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context.getApplicationContext(), ActivityListTitle.class);
            context.startActivity(i);
        } else {
            Toast.makeText(context, "Error en la lectura", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Boolean doInBackground(final String... args) {

        try {
            URL url = new URL(str_url_rss);
            InputStream in = url.openStream();

            if (in != null) {
                XmlPullParserFactory parseCreator = null;
                parseCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser;
                parser = parseCreator.newPullParser();
                parser.setInput(in, null);
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            String tag = parser.getName();
                            if (tag.equalsIgnoreCase("title")) {
                                String title = parser.nextText();
                                MainActivity.list_titles.add(title);
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
}
