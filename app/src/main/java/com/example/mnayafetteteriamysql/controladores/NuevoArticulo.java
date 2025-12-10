package com.example.mnayafetteteriamysql.controladores;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.mnayafetteteriamysql.modelo.Articulo;
import com.example.mnayafetteteriamysql.navegacion.Navegacion;
import com.example.mnayafetteteriamysql.utilidades.Avisos;
import com.example.mnayafetteteriamysql.utilidades.Utilidades;
import com.example.mnayafetteteriamysql.utilidades.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NuevoArticulo extends Navegacion {

    LinearLayout layout;
    EditText nom, desc, pre, st;
    Spinner cat, or;
    RadioButton dest, of;
    String tipoUsuario;

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

        tipoUsuario = tipoUsuario = getSession().getTipo();

        layout = findViewById(R.id.panelNuevoArticulo);
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
        Utilidades.limpiaCampos(layout);
    }

    public void registrar(String url) {

        if (Validaciones.comruebaCamposVacios(layout, NuevoArticulo.this)){
            return;
        }
        if (Validaciones.validarDoublePositivo(pre, NuevoArticulo.this)){
            return;
        }
        if (Validaciones.validarEnteroPositivo(st, NuevoArticulo.this)){
            return;
        }

        Articulo articulo = new Articulo.ArticuloBuilder()
                .setNombre(nom.getText().toString())
                .setCategoria(cat.getSelectedItem().toString())
                .setDescripcion(desc.getText().toString())
                .setPrecio(Double.parseDouble(pre.getText().toString()))
                .setStock(Integer.parseInt(st.getText().toString()))
                .setOrigen(or.getSelectedItem().toString())
                .setOferta(of.isChecked() ? 1 : 2)
                .setDestacado(dest.isChecked() ? 1 : 2)
                .build();

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

                            Avisos.avisoSinBotones(NuevoArticulo.this, getString(R.string.registro_articulo_title),mensaje).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error en la respuesta JSON", Toast.LENGTH_LONG).show();
                        }
                        Utilidades.limpiaCampos(layout);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Avisos.avisoSinBotones(NuevoArticulo.this, getString(R.string.error_database_title),volleyError.toString()).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre", articulo.getNombre());
                parametros.put("categoria", articulo.getCategoria());
                parametros.put("descripcion", articulo.getDescripcion());
                parametros.put("precio", String.valueOf(articulo.getPrecio()));
                parametros.put("stock",String.valueOf(articulo.getStock()));
                parametros.put("origen", articulo.getOrigen());
                parametros.put("oferta", String.valueOf(articulo.getOferta()));
                parametros.put("destacado", String.valueOf(articulo.getDestacado()));

                return parametros;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(NuevoArticulo.this);
        rQueue.getCache().clear();
        rQueue.add(stringRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if ("Admin".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_admin, menu);
        } else if ("User".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_user, menu);
        }

        return true;
    }
}
