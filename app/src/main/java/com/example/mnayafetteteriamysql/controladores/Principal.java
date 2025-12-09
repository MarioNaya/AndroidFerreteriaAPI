package com.example.mnayafetteteriamysql.controladores;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mnayafetteteriamysql.R;
import com.example.mnayafetteteriamysql.adaptadores.ArticulosDestacadosAdapter;
import com.example.mnayafetteteriamysql.modelo.Articulo;
import com.example.mnayafetteteriamysql.navegacion.Navegacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Principal extends Navegacion {

    private ListView listView;
    private ArticulosDestacadosAdapter adapter;

    String usuario, tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarPrincipal));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        usuario = getSession().getNombre();
        tipoUsuario = getSession().getTipo();

        listView = findViewById(R.id.listPrincipal);

        cargarArticulosDestacados();
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

    private void cargarArticulosDestacados() {
        String url = "https://reynaldomd.com/phpscript/listado_articulos_destacados.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                    response -> {
                    List<Articulo> articulos = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String nombre = obj.getString("nombre");
                            String categoria = obj.getString("categoria");
                            String descripcion = obj.getString("descripcion");

                            articulos.add(new Articulo(nombre, categoria, descripcion));
                        }

                        adapter = new ArticulosDestacadosAdapter(this, articulos);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}