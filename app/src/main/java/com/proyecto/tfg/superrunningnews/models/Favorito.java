package com.proyecto.tfg.superrunningnews.models;

public class Favorito {

    private String usuario;
    private String nombreEvento;

    public Favorito() {
    }

    public Favorito(String usuario, String nombreEvento) {
        this.usuario = usuario;
        this.nombreEvento = nombreEvento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }
}
