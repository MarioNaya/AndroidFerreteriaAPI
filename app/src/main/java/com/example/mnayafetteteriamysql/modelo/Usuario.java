package com.example.mnayafetteteriamysql.modelo;

public class Usuario {

    private Long id;
    private String nombre, apellidos, tipo;
    private int edad;
    private String usuario, password;

    public Usuario() {}

    public Usuario(Long id, String nombre, String apellidos, String tipo, int edad, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.edad = edad;
        this.usuario = usuario;
        this.password = password;
    }

    public static class UsuarioBuilder implements IBuilder{

        private Long id;
        private String nombre, apellidos, tipo;
        private int edad;
        private String usuario, password;

        public UsuarioBuilder setId(Long id){
            this.id = id;
            return this;
        }

        public UsuarioBuilder setNombre(String nombre){
            this.nombre = nombre;
            return this;
        }

        public UsuarioBuilder setApellidos(String apellidos){
            this.apellidos = apellidos;
            return this;
        }

        public UsuarioBuilder setTipo(String tipo){
            this.tipo = tipo;
            return this;
        }

        public UsuarioBuilder setEdad(int edad){
            this.edad = edad;
            return this;
        }

        public UsuarioBuilder setUsuario(String usuario){
            this.usuario = usuario;
            return this;
        }

        public UsuarioBuilder setPassword(String password){
            this.password = password;
            return this;
        }

        @Override
        public Usuario build() {
            return new Usuario(id, nombre, apellidos, tipo, edad, usuario, password);
        }
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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
