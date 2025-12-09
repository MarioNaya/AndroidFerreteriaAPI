package com.example.mnayafetteteriamysql.modelo;

public class Usuario {

    private String nombre, apellidos, tipo;
    private int edad;
    private String usuario, password;

    public Usuario(String nombre, String apellidos, String tipo, int edad, String usuario, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.edad = edad;
        this.usuario = usuario;
        this.password = password;
    }

    public Usuario(String nombre, String apellidos, String tipo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
