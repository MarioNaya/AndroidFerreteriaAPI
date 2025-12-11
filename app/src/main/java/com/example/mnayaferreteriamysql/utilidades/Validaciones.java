package com.example.mnayaferreteriamysql.utilidades;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class Validaciones {

    public static boolean comruebaCamposVacios(View view, Context context) {
        return !validarCamposRecursivo(view, context);
    }

    private static boolean validarCamposRecursivo(View view, Context context) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Este campo es obligatorio.");
                editText.requestFocus();
                return false;
            }
        }

        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (spinner.getSelectedItemPosition() == 0) {
                Avisos.campoObligatorio(spinner, context);
                return false;
            }
        }

        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (!validarCamposRecursivo(viewGroup.getChildAt(i), context)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean validarEnteroPositivo(EditText editText, Context context) {
        String texto = editText.getText().toString().trim();

        try {
            int valor = Integer.parseInt(texto);

            if (valor < 0) {
                editText.setError("El valor debe ser 0 o superior");
                editText.requestFocus();
                return true;
            }

            editText.setError(null);
            return false;

        } catch (NumberFormatException e) {
            editText.setError("Debe ingresar un número entero válido");
            editText.requestFocus();
            return true;
        }
    }

    public static boolean validarDoublePositivo(EditText editText, Context context) {
        String texto = editText.getText().toString().trim();

        try {
            double valor = Double.parseDouble(texto);

            if (valor < 0) {
                editText.setError("El valor no puede ser negativo");
                editText.requestFocus();
                return true;
            }

            editText.setError(null);
            return false;

        } catch (NumberFormatException e) {
            editText.setError("Debe ingresar un número decimal válido");
            editText.requestFocus();
            return true;
        }
    }

    public static boolean validarEdad(EditText editText, Context context){
        String texto = editText.getText().toString().trim();

        int valor = Integer.parseInt(texto);

        if (valor < 18 || valor > 99){
            editText.setError("La edad debe estar comprendida entre 18 y 99 años.");
            editText.requestFocus();
            return true;
        }
        return false;
    }
}
