package com.example.mnayafetteteriamysql.controladores;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mnayafetteteriamysql.R;
import com.example.mnayafetteteriamysql.navegacion.Navegacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NuevoArticulo extends Navegacion {

    EditText nom, desc, pre, st;
    Spinner cat, or;
    RadioButton dest, of;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuevo_articulo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarNuevoArticulo));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nom = findViewById(R.id.campoArticulo);
        cat = findViewById(R.id.spinnerCategoria);
        desc = findViewById(R.id.campoDescripcion);
        pre = findViewById(R.id.campoPrecio);
        st = findViewById(R.id.campoStock);
        or = findViewById(R.id.spinnerOrigen);
        dest = findViewById(R.id.radioDestacado);
        of = findViewById(R.id.radioOferta);

        Button boton = findViewById(R.id.btnRegistroArticulo);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar("https://reynaldomd.com/phpscript/registro_articulo.php");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        limpiarCampos();
    }

    public void registrar(String url) {

        String nombre = nom.getText().toString();
        String categoria = cat.getSelectedItem().toString();
        String descripcion = desc.getText().toString();
        String precio = pre.getText().toString();
        String stock = st.getText().toString();
        String origen = or.getSelectedItem().toString();

        int oferta = of.isChecked() ? 1 : 2;
        int destacado = dest.isChecked() ? 1 : 2;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String status = jsonResponse.getString("status");
                            String mensaje = jsonResponse.getString("message");

                            AlertDialog.Builder alerta = new AlertDialog.Builder(NuevoArticulo.this);
                            alerta.setTitle(R.string.registro_articulo_title);
                            alerta.setMessage(mensaje);
                            alerta.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error en la respuesta JSON", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        AlertDialog.Builder alerta = new AlertDialog.Builder(NuevoArticulo.this);
                        alerta.setTitle(R.string.error_database_title);
                        alerta.setMessage(volleyError.toString());
                        alerta.show();
                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("categoria", categoria);
                parametros.put("descripcion", descripcion);
                parametros.put("precio", precio);
                parametros.put("stock",stock);
                parametros.put("origen", origen);
                parametros.put("oferta", String.valueOf(oferta));
                parametros.put("destacado", String.valueOf(destacado));

                return parametros;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(NuevoArticulo.this);
        rQueue.getCache().clear();
        rQueue.add(stringRequest);
    }
    public void limpiarCampos(){
        nom.setText("");
        cat.setSelection(0);
        desc.setText("");
        pre.setText("");
        st.setText("");
        or.setSelection(0);
        dest.setSelected(false);
        of.setSelected(false);
    }
}
