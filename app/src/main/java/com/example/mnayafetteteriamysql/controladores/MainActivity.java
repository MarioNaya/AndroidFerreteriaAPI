package com.example.mnayafetteteriamysql.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.mnayafetteteriamysql.utilidades.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Navegacion {

    EditText user, pass;
    LinearLayout layout;
    public static String usuar, tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSession().haySesionActiva()){
            irAPantallaPrincipal();
            return;
        }

        String url = "https://reynaldomd.com/phpscript/login.php";

        user = findViewById(R.id.campoUsuario);
        pass = findViewById(R.id.campoPass);
        layout = findViewById(R.id.mainLayout);

        Button entrar = findViewById(R.id.btnAcceder);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(url);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.setText("");
        pass.setText("");
        user.requestFocus();
    }

    private void irAPantallaPrincipal() {
        Intent intent = new Intent(MainActivity.this, Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void login(String URL){

        String usuario = user.getText().toString();
        String password = pass.getText().toString();

        if(Validaciones.comruebaCamposVacios(layout, MainActivity.this)){
            StringRequest StringR = new StringRequest(
                    Request.Method.POST,
                    URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String mensaje = null;

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        String status = jsonResponse.getString("status");
                        mensaje = jsonResponse.getString("mensaje");

                        if (Objects.equals(mensaje, "OK")) {
                            tipo = jsonResponse.getString("tipo");
                            usuar = user.getText().toString();
                            getSession().guardarSesion(usuar,tipo);
                            irAPantallaPrincipal();
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setTitle("LOGIN");
                            alerta.setMessage("Error en el logado. Inténtelo de nuevo");
                            alerta.show();

                            user.setText("");
                            pass.setText("");
                            user.requestFocus();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),
                                "Error en la respuesta JSON",
                                Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<String, String>();
                    parametros.put("usuario",usuario);
                    parametros.put("password",password);

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.getCache().clear();
            requestQueue.add(StringR);
        }
        }

}