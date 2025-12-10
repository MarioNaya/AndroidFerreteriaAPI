package com.example.mnayafetteteriamysql.modelo;

public class Articulo {

    private Long id;
    private String nombre, categoria, descripcion;
    private double precio;
    private int stock;
    private String origen;
    private int destacado;
    private int oferta;

    public Articulo(){}

    public Articulo(Long id, String nombre, String categoria, String descripcion, double precio, int stock, String origen, int destacado, int oferta) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.origen = origen;
        this.destacado = destacado;
        this.oferta = oferta;
    }

    public static class ArticuloBuilder implements IBuilder{

        private Long id;
        private String nombre, categoria, descripcion;
        private double precio;
        private int stock;
        private String origen;
        private int destacado;
        private int oferta;

        public ArticuloBuilder() {
        }

        public ArticuloBuilder setId(Long id){
            this.id = id;
            return this;
        }

        public ArticuloBuilder setNombre(String nombre){
            this.nombre = nombre;
            return this;
        }

        public ArticuloBuilder setCategoria(String categoria){
            this.categoria = categoria;
            return this;
        }

        public ArticuloBuilder setDescripcion(String descripcion){
            this.descripcion = descripcion;
            return this;
        }

        public ArticuloBuilder setPrecio(double precio){
            this.precio = precio;
            return this;
        }

        public ArticuloBuilder setStock(int stock){
            this.stock = stock;
            return this;
        }

        public ArticuloBuilder setOrigen(String origen){
            this.origen = origen;
            return this;
        }

        public ArticuloBuilder setDestacado(int destacado){
            this.destacado = destacado;
            return this;
        }

        public ArticuloBuilder setOferta(int oferta){
            this.oferta = oferta;
            return this;
        }

        @Override
        public Articulo build() {
            return new Articulo(id, nombre, categoria, descripcion, precio, stock, origen, destacado, oferta);
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getOrigen() { return origen; }

    public void setOrigen(String origen) { this.origen = origen; }

    public int getDestacado() {
        return destacado;
    }

    public void setDestacado(int destacado) {
        this.destacado = destacado;
    }

    public int getOferta() {
        return oferta;
    }

    public void setOferta(int oferta) {
        this.oferta = oferta;
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", destacado=" + destacado +
                ", oferta=" + oferta +
                '}';
    }
}
