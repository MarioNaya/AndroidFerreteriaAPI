package com.example.mnayaferreteriamysql.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_TIPO = "tipo";
    private static final String KEY_CATEGORIA = "ultima_categoria";

    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private SessionManager(Context context){
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static SessionManager getInstance(Context context){
        if (instance == null){
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void guardarSesion(String nombre, String tipo){
        editor.putString(KEY_NOMBRE,nombre);
        editor.putString(KEY_TIPO,tipo);
        editor.apply();
    }

    public String getNombre(){
        return prefs.getString("nombre","");
    }

    public String getTipo(){
        return prefs.getString("tipo","");
    }

    public boolean haySesionActiva(){
        return !prefs.getString(KEY_NOMBRE,"").isEmpty();
    }

    public void guardarCategoria(String categoria){
        editor.putString(KEY_CATEGORIA, categoria);
        editor.apply();
    }

    public String getCategoria(){
        return prefs.getString(KEY_CATEGORIA, null);
    }

    public void cerrarSesion(){
        editor.clear();
        editor.apply();
    }
}
