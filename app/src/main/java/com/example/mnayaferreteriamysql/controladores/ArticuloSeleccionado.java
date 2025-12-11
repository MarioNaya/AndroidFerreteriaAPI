package com.example.mnayaferreteriamysql.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.modelo.Articulo;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;
import com.example.mnayaferreteriamysql.utilidades.Avisos;
import com.example.mnayaferreteriamysql.utilidades.Validaciones;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ArticuloSeleccionado extends Navegacion {

    private static final String TAG = "ArticuloSeleccionado";
    private static final String URL_DETALLE = "https://reynaldomd.com/phpscript/rescata_articulo.php";

    private TextView textIdDetalle;
    private TextView textNombreDetalle;
    private TextView textCategoriaDetalle;
    private TextView textDescripcionDetalle;
    private TextInputEditText campoPrecioDetalle;
    private TextInputEditText campoStockDetalle;
    private TextView textOrigenDetalle;
    private String tipoUsuario;
    private String idArticulo;
    private Articulo articulo;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnUpdate;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_articulo_seleccionado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarDetalleArticulo));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = getSession().getTipo();

        inicializarVistas();
        obtenerIdArticulo();
        cargarDetalleArticulo();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarCampos(true);
                campoPrecioDetalle.requestFocus();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validaciones.comruebaCamposVacios(layout, ArticuloSeleccionado.this)){
                    return;
                }

                String url = "https://reynaldomd.com/phpscript/actualiza_articulo.php";

                String id = textIdDetalle.getText().toString().replace("#", "").trim();
                String precio = campoPrecioDetalle.getText().toString();
                String stock = campoStockDetalle.getText().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    String mensaje = jsonResponse.getString("message");
                                    Avisos.avisoSinBotones(ArticuloSeleccionado.this, getString(R.string.registro_articulo_title), mensaje);
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Error al procesar respuesta: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),
                                        "Error de conexión: " + error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);
                        params.put("precio", precio);
                        params.put("stock", stock);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(ArticuloSeleccionado.this);
                requestQueue.getCache().clear();
                requestQueue.add(stringRequest);
                activarCampos(false);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avisos.avisoOkCancel(
                        ArticuloSeleccionado.this,
                        getString(R.string.eliminar_articulo_title),
                        getString(R.string.eliminar_articulo_message),
                        ()-> eliminarArticulo());
            }
        });
    }

    private void inicializarVistas() {
        textIdDetalle = findViewById(R.id.textIdDetalleArticulo);
        textNombreDetalle = findViewById(R.id.textNombreDetalleArticulo);
        textCategoriaDetalle = findViewById(R.id.textCategoriaDetalleArticulo);
        textDescripcionDetalle = findViewById(R.id.textDescripcionDetalleArticulo);
        campoPrecioDetalle = findViewById(R.id.campoPrecioDetalleArticulo);
        campoStockDetalle = findViewById(R.id.campoStockDetalleArticulo);
        textOrigenDetalle = findViewById(R.id.textOrigenDetalleArticulo);
        btnEdit = findViewById(R.id.btnEditDetalleArticulo);
        btnUpdate = findViewById(R.id.btnUpdateDetalleArticulo);
        btnDelete = findViewById(R.id.btnDeleteDetalleArticulo);
        layout = findViewById(R.id.panelDetalleArticulo);

        configurarVisibilidadBotones();
    }

    private void configurarVisibilidadBotones() {

        if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase("Admin")) {
            // Mostrar botones para administradores
            findViewById(R.id.actionButtonsContainer).setVisibility(View.VISIBLE);
        } else {
            // Ocultar botones para usuarios normales
            findViewById(R.id.actionButtonsContainer).setVisibility(View.GONE);
        }
    }

    private void activarCampos(Boolean bool){
        campoPrecioDetalle.setEnabled(bool);
        campoStockDetalle.setEnabled(bool);
        btnUpdate.setEnabled(bool);
    }

    private void obtenerIdArticulo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idArticulo = extras.getString("id");
            if (idArticulo == null || idArticulo.isEmpty()) {
                Toast.makeText(this, "Error: ID de artículo no válido", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: No se recibió el ID del artículo", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDetalleArticulo() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DETALLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error en la petición: " + error.toString());
                        Toast.makeText(ArticuloSeleccionado.this,
                                "Error al cargar los datos del artículo",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", idArticulo);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void procesarRespuesta(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("status") && jsonResponse.getString("status").equals("error")) {
                String mensaje = jsonResponse.getString("mensaje");
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            articulo = new Articulo.ArticuloBuilder()
                    .setId(jsonResponse.getLong("id"))
                    .setNombre(jsonResponse.getString("nombre"))
                    .setCategoria(jsonResponse.getString("categoria"))
                    .setDescripcion(jsonResponse.getString("descripcion"))
                    .setPrecio(jsonResponse.getDouble("precio"))
                    .setStock(jsonResponse.getInt("stock"))
                    .setOrigen(jsonResponse.getString("origen"))
                    .build();

            mostrarDatosArticulo();

        } catch (JSONException e) {
            Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if ("Admin".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_admin, menu);
        } else if ("User".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_user, menu);
        }

        return true;
    }

    private void mostrarDatosArticulo() {
        textIdDetalle.setText("#" + articulo.getId());
        textNombreDetalle.setText(articulo.getNombre().toUpperCase());
        textCategoriaDetalle.setText(articulo.getCategoria());
        textDescripcionDetalle.setText(articulo.getDescripcion());
        campoPrecioDetalle.setText(String.valueOf(articulo.getPrecio()));
        campoStockDetalle.setText(String.valueOf(articulo.getStock()));
        textOrigenDetalle.setText(articulo.getOrigen());
    }

    private void eliminarArticulo() {
        String url = "https://reynaldomd.com/phpscript/elimina_articulo.php";

        // Obtener el ID sin el símbolo #
        String id = textIdDetalle.getText().toString().replace("#", "").trim();

        if (id.isEmpty()) {
            Toast.makeText(ArticuloSeleccionado.this,
                    "Error: ID no válido",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(ArticuloSeleccionado.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error al procesar respuesta: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Error de conexión: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ArticuloSeleccionado.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        Intent intent = new Intent(ArticuloSeleccionado.this, ListadoArticulos.class);
        startActivity(intent);
        finish();
    }
}