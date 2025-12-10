package com.example.mnayafetteteriamysql.utilidades;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class Utilidades {

    public static void limpiaCampos(View view) {

        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }

        if (view instanceof Spinner) {
            ((Spinner) view).setSelection(0);
        }

        if (view instanceof RadioButton) {
            ((RadioButton) view).setChecked(false);
        }

        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup grupo = (android.view.ViewGroup) view;
            for (int i = 0; i < grupo.getChildCount(); i++) {
                limpiaCampos(grupo.getChildAt(i));
            }
        }
    }

}
