package com.example.mnayaferreteriamysql.controladores;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Cuenta extends Navegacion {

    private static final String TAG = "Cuenta";
    private static final String URL_DETALLE = "https://reynaldomd.com/phpscript/rescata_usuario.php";

    private ImageView imageAvatar;
    private TextView textNombreCompleto;
    private TextView textUserCuenta;
    private TextView textTipoCuenta;
    private TextInputEditText campoNombreCuenta;
    private TextInputEditText campoApellidosCuenta;
    private TextInputEditText campoEdadCuenta;
    private TextInputEditText campoPasswordCuenta;
    private Button btnEdit;
    private Button btnUpdate;
    private String usuarioActual;
    private Usuario usuario;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarCuenta));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarVistas();
        obtenerUsuarioActual();
        cargarDatosCuenta();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarCampos(true);
                campoNombreCuenta.requestFocus();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validaciones.comruebaCamposVacios(layout, Cuenta.this)){
                    return;
                }

                actualizarCuenta();
            }
        });
    }

    private void inicializarVistas() {
        imageAvatar = findViewById(R.id.imageAvatarCuenta);
        textNombreCompleto = findViewById(R.id.textNombreCompletoCuenta);
        textUserCuenta = findViewById(R.id.textUserCuenta);
        textTipoCuenta = findViewById(R.id.textTipoCuenta);
        campoNombreCuenta = findViewById(R.id.campoNombreCuenta);
        campoApellidosCuenta = findViewById(R.id.campoApellidosCuenta);
        campoEdadCuenta = findViewById(R.id.campoEdadCuenta);
        campoPasswordCuenta = findViewById(R.id.campoPasswordCuenta);
        btnEdit = findViewById(R.id.btnEditCuenta);
        btnUpdate = findViewById(R.id.btnUpdateCuenta);
        layout = findViewById(R.id.main);

        activarCampos(false);
    }

    private void activarCampos(Boolean bool) {
        campoNombreCuenta.setEnabled(bool);
        campoApellidosCuenta.setEnabled(bool);
        campoEdadCuenta.setEnabled(bool);
        campoPasswordCuenta.setEnabled(bool);
        btnUpdate.setEnabled(bool);
    }

    private void obtenerUsuarioActual() {
        try {
            usuarioActual = getSession().getNombre();
            Log.d(TAG, "Usuario obtenido de sesión: " + usuarioActual);

            if (usuarioActual == null || usuarioActual.isEmpty()) {
                Log.e(TAG, "Usuario es null o vacío");
                Toast.makeText(this, "Error: No se pudo obtener el usuario actual", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener usuario: " + e.getMessage());
            Toast.makeText(this, "Error al acceder a la sesión", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDatosCuenta() {
        Log.d(TAG, "Iniciando carga de datos para usuario: " + usuarioActual);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DETALLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respuesta recibida: " + response);
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error en la petición: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e(TAG, "Código de error: " + error.networkResponse.statusCode);
                        }
                        Toast.makeText(Cuenta.this,
                                "Error al cargar los datos de la cuenta",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuarioActual);
                Log.d(TAG, "Enviando parámetro usuario: " + usuarioActual);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void procesarRespuesta(String response) {
        try {
            Log.d(TAG, "Procesando respuesta JSON");
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("status") && jsonResponse.getString("status").equals("error")) {
                String mensaje = jsonResponse.getString("mensaje");
                Log.e(TAG, "Error en respuesta: " + mensaje);
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

            Log.d(TAG, "Usuario creado correctamente: " + usuario.getNombre());
            mostrarDatosCuenta();

        } catch (JSONException e) {
            Log.e(TAG, "Error al parsear JSON: " + e.getMessage());
            Toast.makeText(this, "Error al procesar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado: " + e.getMessage());
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDatosCuenta() {
        // Actualizar campos del header
        textNombreCompleto.setText(usuario.getNombre() + " " + usuario.getApellidos());
        textUserCuenta.setText(usuario.getUsuario());
        textTipoCuenta.setText(usuario.getTipo().toUpperCase());

        // Actualizar campos editables
        campoNombreCuenta.setText(usuario.getNombre());
        campoApellidosCuenta.setText(usuario.getApellidos());
        campoEdadCuenta.setText(String.valueOf(usuario.getEdad()));
        campoPasswordCuenta.setText(usuario.getPassword());

        if (usuario.getTipo().equalsIgnoreCase("Admin") ||
                usuario.getTipo().equalsIgnoreCase("Administrador") ||
                usuario.getTipo().equalsIgnoreCase("admin") ||
                usuario.getTipo().equalsIgnoreCase("administrador")) {
            imageAvatar.setImageResource(R.drawable.pictogramaadmin);
        } else if (usuario.getTipo().equalsIgnoreCase("User") ||
                usuario.getTipo().equalsIgnoreCase("Usuario") ||
                usuario.getTipo().equalsIgnoreCase("user") ||
                usuario.getTipo().equalsIgnoreCase("usuario")) {
            imageAvatar.setImageResource(R.drawable.pictogramauser);
        }
    }

    private void actualizarCuenta() {
        String url = "https://reynaldomd.com/phpscript/actualiza_usuario.php";

        String usuario = usuarioActual;
        String nombre = campoNombreCuenta.getText().toString().trim();
        String apellidos = campoApellidosCuenta.getText().toString().trim();
        String edad = campoEdadCuenta.getText().toString().trim();
        String password = campoPasswordCuenta.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String mensaje = jsonResponse.getString("message");

                            if (status.equals("success")) {
                                textNombreCompleto.setText(nombre + " " + apellidos);

                                Avisos.avisoSinBotones(Cuenta.this,
                                        getString(R.string.title_cuenta),
                                        mensaje);

                                cargarDatosCuenta();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + mensaje,
                                        Toast.LENGTH_LONG).show();
                            }
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

        RequestQueue requestQueue = Volley.newRequestQueue(Cuenta.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        activarCampos(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        String tipoUsuario = getSession().getTipo();

        if ("Admin".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_admin, menu);
        } else if ("User".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_user, menu);
        }

        return true;
    }
}