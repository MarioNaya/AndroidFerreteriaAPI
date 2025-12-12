package com.example.mnayaferreteriamysql.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.adaptadores.ListaUsuariosAdapter;
import com.example.mnayaferreteriamysql.modelo.Usuario;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListadoUsuarios extends Navegacion {

    private ListView listView;
    private ListaUsuariosAdapter adapter;
    private String tipoUsuario;
    private List<Usuario> usuarios;
    private TextView textCountUsuarios;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarListUsuarios));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = getSession().getTipo();

        listView = findViewById(R.id.listUsuarios);
        textCountUsuarios = findViewById(R.id.textCountUsuarios);

        cargarUsuarios();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuarioSeleccionado = usuarios.get(position);

                Intent intent = new Intent(ListadoUsuarios.this, UsuarioSeleccionado.class);
                intent.putExtra("usuario", usuarioSeleccionado.getUsuario());
                startActivity(intent);
            }
        });
        }

        @Override
        protected void onResume () {
            cargarUsuarios();
            super.onResume();
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();

            if ("Admin".equals(tipoUsuario)) {
                inflater.inflate(R.menu.menu_admin, menu);
            } else if ("User".equals(tipoUsuario)) {
                inflater.inflate(R.menu.menu_user, menu);
            }

            return true;
        }

        private void cargarUsuarios () {

            String url = "https://reynaldomd.com/phpscript/listado_usuarios.php";

            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        usuarios = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                usuarios.add(
                                        new Usuario.UsuarioBuilder()
                                                .setNombre(obj.getString("nombre"))
                                                .setApellidos(obj.getString("apellidos"))
                                                .setUsuario(obj.getString("usuario"))
                                                .setTipo(obj.getString("tipo"))
                                                .build()
                                );
                            }
                            adapter = new ListaUsuariosAdapter(this, usuarios);
                            listView.setAdapter(adapter);
                            textCountUsuarios.setText("Total usuarios: " + usuarios.size());

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, volleyError -> {
                Toast.makeText(this, getString(R.string.error_carga_datos), Toast.LENGTH_SHORT).show();
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(ListadoUsuarios.this);
            requestQueue.getCache().clear();
            requestQueue.add(request);
        }
    }