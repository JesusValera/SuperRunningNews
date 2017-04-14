package com.proyecto.tfg.superrunningnews.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Noticia implements Parcelable {

    /** TODO Borrar esto en el futuro.
    * Usado: http://www.parcelabler.com/
    * Mas info: http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable
    */

    private String titulo;
    private String imagen;
    private String localizacion;
    private String fecha;
    private String link;

    public Noticia() {
        this.titulo = "";
        this.imagen = "";
        this.localizacion = "";
        this.fecha = "";
        this.link = "";
    }

    public Noticia(String titulo, String imagen, String localizacion, String fecha, String link) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.localizacion = localizacion;
        this.fecha = fecha;
        this.link = link;
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

    @Override
    public String toString() {
        return "Titulo: " + titulo + '\n' +
                "Imagen: " + imagen + '\n' +
                "Localizacion: " + localizacion + '\n' +
                "Fecha: " + fecha + '\n' +
                "Link: " + link + '\n';
    }

    // Parte parcelable.
    protected Noticia(Parcel in) {
        titulo = in.readString();
        imagen = in.readString();
        localizacion = in.readString();
        fecha = in.readString();
        link = in.readString();
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
