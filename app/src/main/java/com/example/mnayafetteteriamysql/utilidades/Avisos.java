package com.example.mnayafetteteriamysql.utilidades;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.mnayafetteteriamysql.R;

public class Avisos {

    public static AlertDialog.Builder campoObligatorio(View campo, Context context){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle(R.string.campo_obligatorio_title);
        aviso.setMessage(String.format("El campo %s es obligatorio",campo.getTag()));
        aviso.setCancelable(true);
        return aviso;
    }

    public static AlertDialog.Builder avisoSinBotones(Context context, String titulo, String mensaje){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle(titulo);
        aviso.setMessage(mensaje);
        aviso.setCancelable(true);
        return aviso;
    }
}
