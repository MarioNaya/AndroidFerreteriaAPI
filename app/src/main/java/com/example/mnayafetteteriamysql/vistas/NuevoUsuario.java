package com.example.mnayafetteteriamysql.vistas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class NuevoUsuario extends Navegacion {

    EditText nom, ape, eda, usu, pas;
    Spinner tip;

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
        limpiarCampos();
    }

    public void registrar(String url){

        String nombre = nom.getText().toString();
        String apellidos = ape.getText().toString();
        String edad = eda.getText().toString();
        String usuario = usu.getText().toString();
        String password = pas.getText().toString();
        String tipo = tip.getSelectedItem().toString();

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

                            AlertDialog.Builder alerta = new AlertDialog.Builder(NuevoUsuario.this);
                            alerta.setTitle(R.string.nuevo_usuario_title);
                            alerta.setMessage(mensaje);
                            alerta.show();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(NuevoUsuario.this);
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
                parametros.put("nombre",nombre);
                parametros.put("apellidos",apellidos);
                parametros.put("edad",edad);
                parametros.put("usuario",usuario);
                parametros.put("password",password);
                parametros.put("tipo",tipo);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NuevoUsuario.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void limpiarCampos(){
        nom.setText("");
        ape.setText("");
        eda.setText("");
        usu.setText("");
        pas.setText("");
        tip.setSelection(0);
    }
}