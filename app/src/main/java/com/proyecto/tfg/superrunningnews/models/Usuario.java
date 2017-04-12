package com.proyecto.tfg.superrunningnews.models;


public class Usuario {

    private String nombre;
    private String password;
    // array de String de las carreras marcadas como favoritas.
    // String con URL de img para chat? -> Eso si le metemos imagen al chat (propia y otro usuario, solo otro o ninguna img)... (?)

    public Usuario() {
        this.nombre = "";
        this.password= "";
    }

    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
