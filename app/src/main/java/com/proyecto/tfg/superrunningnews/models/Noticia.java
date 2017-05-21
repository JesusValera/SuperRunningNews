package com.proyecto.tfg.superrunningnews.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Noticia implements Parcelable, Comparable<Noticia> {

    /**
     * Usado: http://www.parcelabler.com/
     * Mas info: http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable
     */

    private String titulo;
    private String imagen;
    private String localizacion;
    private String fecha;
    private String link;
    private LatLng latLng;
    private boolean favorito;

    public Noticia() {
        this.titulo = "";
        this.imagen = "";
        this.localizacion = "";
        this.fecha = "";
        this.link = "";
        this.latLng = null;
        favorito = false;
    }

    public Noticia(String titulo, String imagen, String localizacion, String fecha, String link, LatLng latLng) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.localizacion = localizacion;
        this.fecha = fecha;
        this.link = link;
        this.latLng = latLng;
        favorito = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + '\n' +
                "Imagen: " + imagen + '\n' +
                "Localizacion: " + localizacion + '\n' +
                "Fecha: " + fecha + '\n' +
                "Link: " + link + '\n';
    }

    @Override
    public int compareTo(@NonNull Noticia n) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaThis = null, fechaN = null;
        try {
            fechaThis = formatter.parse(this.fecha);
            fechaN = formatter.parse(n.fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fechaThis.before(fechaN)) {
            return 1;
        } else if (fechaThis.equals(fechaN)) {
            return 0;
        } else {
            return -1;
        }
    }

    public static Comparator<Noticia> NoticiaOrdenadaFechaInversa = new Comparator<Noticia>() {
        @Override
        public int compare(Noticia n1, Noticia n2) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaThis = null, fechaN = null;
            try {
                fechaThis = formatter.parse(n1.fecha);
                fechaN = formatter.parse(n2.fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (fechaThis.before(fechaN)) {
                return -1;
            } else if (fechaThis.equals(fechaN)) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public static Comparator<Noticia> NoticiaOrdenadaProvincia = new Comparator<Noticia>() {
        @Override
        public int compare(Noticia n1, Noticia n2) {
            return n1.getLocalizacion().compareToIgnoreCase(n2.getLocalizacion());
        }
    };

    // Parte parcelable.
    protected Noticia(Parcel in) {
        titulo = in.readString();
        imagen = in.readString();
        localizacion = in.readString();
        fecha = in.readString();
        link = in.readString();
        latLng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        favorito = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(imagen);
        dest.writeString(localizacion);
        dest.writeString(fecha);
        dest.writeString(link);
        dest.writeValue(latLng);
        dest.writeByte((byte) (favorito ? 1 : 0));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Noticia> CREATOR = new Parcelable.Creator<Noticia>() {
        @Override
        public Noticia createFromParcel(Parcel in) {
            return new Noticia(in);
        }

        @Override
        public Noticia[] newArray(int size) {
            return new Noticia[size];
        }
    };

}
