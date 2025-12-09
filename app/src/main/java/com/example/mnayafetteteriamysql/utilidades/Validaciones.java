package com.example.mnayafetteteriamysql.utilidades;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class Validaciones {

    public static boolean comruebaCamposVacios(LinearLayout layout, Context context) {

        for (int i = 0; i < layout.getChildCount(); i++) {
            View campo = layout.getChildAt(i);
            if (campo instanceof EditText && ((EditText) campo).getText().toString().trim().isEmpty()) {
                Avisos.campoObligatorio(campo, context).show();
                return false;
            }

            if (campo instanceof Spinner && ((Spinner) campo).getSelectedItemPosition() == 0) {
                Avisos.campoObligatorio(campo, context).show();
                return false;
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
                return false;
            }

            editText.setError(null);
            return true;

        } catch (NumberFormatException e) {
            editText.setError("Debe ingresar un número entero válido");
            editText.requestFocus();
            return false;
        }
    }

    public static boolean validarDoublePositivo(EditText editText, Context context) {
        String texto = editText.getText().toString().trim();

        try {
            double valor = Double.parseDouble(texto);

            if (valor < 0) {
                editText.setError("El valor no puede ser negativo");
                editText.requestFocus();
                return false;
            }

            editText.setError(null);
            return true;

        } catch (NumberFormatException e) {
            editText.setError("Debe ingresar un número decimal válido");
            editText.requestFocus();
            return false;
        }
    }
}
