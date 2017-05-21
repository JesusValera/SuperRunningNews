package com.proyecto.tfg.superrunningnews;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificacionesService extends IntentService {

    private ArrayList<Noticia> tNoticias;
    private int idNotificacion;

    public NotificacionesService() {
        super("NotificacionesService");
        idNotificacion = 0;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            //tNoticias = intent.getParcelableArrayListExtra("noticia"); //El intent no tiene los datos
            tNoticias=BottomBarActivity.noticias;
            Calendar fechaNoticia = Calendar.getInstance();
            Calendar fechaActual = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Noticia n : tNoticias) {
                try {
                    fechaNoticia.setTime(sdf.parse(n.getFecha()));
                    int diaNoticia = fechaNoticia.get(Calendar.DAY_OF_YEAR);
                    int hoy = fechaActual.get(Calendar.DAY_OF_YEAR);

                    //System.out.println(n.getTitulo()+"\nhoy: "+hoy+"  , diaNoticia:"+diaNoticia);

                    if (diaNoticia - hoy == 1) {
                        montarNotificacion(n, "¡Mañana es el Evento!");
                    } else if (diaNoticia - hoy == 7) {
                        montarNotificacion(n, "¡La semana que viene es el evento!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void montarNotificacion(Noticia noticia, String texto) throws Exception{
        Bitmap bitmap=Picasso.with(getApplicationContext()).load(noticia.getImagen()).get();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(NotificacionesService.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(noticia.getTitulo())
                        .setContentText(texto)
                        .setAutoCancel(true);

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(noticia.getLink()));
        webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        mBuilder.setLargeIcon(bitmap);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        NotificacionesService.this,
                        0,
                        webIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(idNotificacion, mBuilder.build());
        idNotificacion++;
    }

}
