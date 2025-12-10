package com.example.mnayaferreteriamysql.controladores;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.modelo.Usuario;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;
import com.example.mnayaferreteriamysql.utilidades.Avisos;
import com.example.mnayaferreteriamysql.utilidades.Utilidades;
import com.example.mnayaferreteriamysql.utilidades.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NuevoUsuario extends Navegacion {

    LinearLayout layout;
    EditText nom, ape, eda, usu, pas;
    Spinner tip;
    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuevo_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarNuevoUsuario));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = tipoUsuario = getSession().getTipo();

        layout = findViewById(R.id.panelNuevoUsuario);

        nom = findViewById(R.id.campoNombreUser);
        ape = findViewById(R.id.campoApellidosUser);
        eda = findViewById(R.id.campoEdadUser);
        usu = findViewById(R.id.campoUsuarioUser);
        pas = findViewById(R.id.campoPassUser);
        tip = findViewById(R.id.spinnerTipoUser);

        Button boton = findViewById(R.id.btnRegistrarUsuario);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar("https://reynaldomd.com/phpscript/registro_usuario.php");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilidades.limpiaCampos(layout);
    }

    public void registrar(String url){

        if (Validaciones.comruebaCamposVacios(layout, NuevoUsuario.this)){
            return;
        }
        if (Validaciones.validarEnteroPositivo(eda, NuevoUsuario.this)){
            return;
        }
        if (Validaciones.validarEdad(eda, NuevoUsuario.this)){
            return;
        }
        Usuario usuario = new Usuario.UsuarioBuilder()
                .setNombre(nom.getText().toString())
                .setApellidos(ape.getText().toString())
                .setEdad(Integer.parseInt(eda.getText().toString()))
                .setUsuario(usu.getText().toString())
                .setPassword(pas.getText().toString())
                .setTipo(tip.getSelectedItem().toString())
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

                            Avisos.avisoSinBotones(NuevoUsuario.this,getString(R.string.nuevo_usuario_title),mensaje).show();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Utilidades.limpiaCampos(layout);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Avisos.avisoSinBotones(NuevoUsuario.this,getString(R.string.error_database_title),volleyError.toString()).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre",usuario.getNombre());
                parametros.put("apellidos",usuario.getApellidos());
                parametros.put("edad",String.valueOf(usuario.getEdad()));
                parametros.put("usuario",usuario.getUsuario());
                parametros.put("password",usuario.getPassword());
                parametros.put("tipo",usuario.getTipo());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NuevoUsuario.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
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