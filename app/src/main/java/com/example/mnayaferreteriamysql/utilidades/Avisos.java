package com.example.mnayaferreteriamysql.utilidades;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.mnayaferreteriamysql.R;


public class Avisos {

    public static void campoObligatorio(View campo, Context context){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle(R.string.campo_obligatorio_title);
        aviso.setMessage(String.format("El campo %s es obligatorio",campo.getTag()));
        aviso.setCancelable(true);
        aviso.show();
    }

    public static void avisoSinBotones(Context context, String titulo, String mensaje){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle(titulo);
        aviso.setMessage(mensaje);
        aviso.setCancelable(true);
        aviso.show();
    }

    public static  void avisoOkCancel(Context context, String titulo, String mensaje, EjecutarFuncion ejecutarFuncion){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle(titulo);
        aviso.setMessage(mensaje);
        aviso.setPositiveButton("OK", (dialog, which) -> {
            if (ejecutarFuncion != null) ejecutarFuncion.ejecutar();
        });

        aviso.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        aviso.show();
    }
}
