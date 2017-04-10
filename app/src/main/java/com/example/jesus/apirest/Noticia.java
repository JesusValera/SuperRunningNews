package com.example.jesus.apirest;



public class Noticia {

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
}
