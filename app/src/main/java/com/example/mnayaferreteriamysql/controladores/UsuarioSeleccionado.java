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
import com.example.mnayaferreteriamysql.modelo.Usuario;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;
import com.example.mnayaferreteriamysql.utilidades.Avisos;
import com.example.mnayaferreteriamysql.utilidades.Validaciones;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UsuarioSeleccionado extends Navegacion {

    private static final String TAG = "UsuarioSeleccionado";
    private static final String URL_DETALLE = "https://reynaldomd.com/phpscript/rescata_usuario.php";

    private TextInputEditText campoNombreDetalle;
    private TextInputEditText campoApellidosDetalle;
    private TextInputEditText campoEdadDetalle;
    private TextView textUsuarioDetalle;
    private TextInputEditText campoPasswordDetalle;
    private TextView textTipoDetalle;
    private String tipoUsuario;
    private String usuarioSeleccionado;
    private Usuario usuario;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnUpdate;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_seleccionado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarDetalleUsuario));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = getSession().getTipo();

        inicializarVistas();
        obtenerUsuario();
        cargarDetalleUsuario();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarCampos(true);
                campoNombreDetalle.requestFocus();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validaciones.comruebaCamposVacios(layout, UsuarioSeleccionado.this)){
                    return;
                }

                if (Validaciones.validarEnteroPositivo(campoEdadDetalle, UsuarioSeleccionado.this)){
                    return;
                }

                if (Validaciones.validarEdad(campoEdadDetalle, UsuarioSeleccionado.this)){
                    return;
                }

                String url = "https://reynaldomd.com/phpscript/actualiza_usuario.php";

                String usuario = textUsuarioDetalle.getText().toString().replace("@", "").trim();
                String nombre = campoNombreDetalle.getText().toString();
                String apellidos = campoApellidosDetalle.getText().toString();
                String edad = campoEdadDetalle.getText().toString();
                String password = campoPasswordDetalle.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    String mensaje = jsonResponse.getString("message");
                                    Avisos.avisoSinBotones(UsuarioSeleccionado.this, getString(R.string.registro_usuario_title), mensaje);
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
                        params.put("usuario", usuario);
                        params.put("nombre", nombre);
                        params.put("apellidos", apellidos);
                        params.put("edad", edad);
                        params.put("password", password);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(UsuarioSeleccionado.this);
                requestQueue.getCache().clear();
                requestQueue.add(stringRequest);
                activarCampos(false);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avisos.avisoOkCancel(
                        UsuarioSeleccionado.this,
                        getString(R.string.eliminar_usuario_title),
                        getString(R.string.eliminar_usuario_message),
                        ()-> eliminarUsuario());
            }
        });
    }

    private void inicializarVistas() {
        campoNombreDetalle = findViewById(R.id.campoNombreDetalleUsuario);
        campoApellidosDetalle = findViewById(R.id.campoApellidosDetalleUsuario);
        campoEdadDetalle = findViewById(R.id.campoEdadDetalleUsuario);
        textUsuarioDetalle = findViewById(R.id.textUsuarioDetalleUsuario);
        campoPasswordDetalle = findViewById(R.id.campoPasswordDetalleUsuario);
        textTipoDetalle = findViewById(R.id.textTipoDetalleUsuario);
        btnEdit = findViewById(R.id.btnEditDetalleUsuario);
        btnUpdate = findViewById(R.id.btnUpdateDetalleUsuario);
        btnDelete = findViewById(R.id.btnDeleteDetalleUsuario);
        layout = findViewById(R.id.panelDetalleUsuario);

        configurarVisibilidadBotones();
    }

    private void configurarVisibilidadBotones() {
        if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase("Admin")) {
            findViewById(R.id.actionButtonsContainer).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.actionButtonsContainer).setVisibility(View.GONE);
        }
    }

    private void activarCampos(Boolean bool){
        campoNombreDetalle.setEnabled(bool);
        campoApellidosDetalle.setEnabled(bool);
        campoEdadDetalle.setEnabled(bool);
        campoPasswordDetalle.setEnabled(bool);
        btnUpdate.setEnabled(bool);
    }

    private void obtenerUsuario() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuarioSeleccionado = extras.getString("usuario");
            if (usuarioSeleccionado == null || usuarioSeleccionado.isEmpty()) {
                Toast.makeText(this, "Error: Usuario no válido", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: No se recibió el usuario", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDetalleUsuario() {
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
                        Toast.makeText(UsuarioSeleccionado.this,
                                "Error al cargar los datos del usuario",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuarioSeleccionado);
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

            usuario = new Usuario.UsuarioBuilder()
                    .setNombre(jsonResponse.getString("nombre"))
                    .setApellidos(jsonResponse.getString("apellidos"))
                    .setEdad(jsonResponse.getInt("edad"))
                    .setUsuario(jsonResponse.getString("usuario"))
                    .setPassword(jsonResponse.getString("password"))
                    .setTipo(jsonResponse.getString("tipo"))
                    .build();

            mostrarDatosUsuario();

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

    private void mostrarDatosUsuario() {
        campoNombreDetalle.setText(usuario.getNombre());
        campoApellidosDetalle.setText(usuario.getApellidos());
        campoEdadDetalle.setText(String.valueOf(usuario.getEdad()));
        textUsuarioDetalle.setText(usuario.getUsuario());
        campoPasswordDetalle.setText(usuario.getPassword());
        textTipoDetalle.setText(usuario.getTipo().toUpperCase());
    }

    private void eliminarUsuario() {
        String url = "https://reynaldomd.com/phpscript/elimina_usuario.php";

        String usuario = textUsuarioDetalle.getText().toString().replace("@", "").trim();

        if (usuario.isEmpty()) {
            Toast.makeText(UsuarioSeleccionado.this,
                    "Error: Usuario no válido",
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

                            Toast.makeText(UsuarioSeleccionado.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("usuario", usuario);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UsuarioSeleccionado.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        Intent intent = new Intent(UsuarioSeleccionado.this, ListadoUsuarios.class);
        startActivity(intent);
        finish();
    }
}