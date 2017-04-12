package com.example.jesus.apirest.models;


public class Usuario {

    private String nombre;
    private String password;

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
