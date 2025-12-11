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
import com.example.mnayaferreteriamysql.adaptadores.ListaArticulosAdapter;
import com.example.mnayaferreteriamysql.modelo.Articulo;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListadoArticulos extends Navegacion {

    private ListView listView;
    private ListaArticulosAdapter adapter;
    String tipoUsuario;
    List<Articulo> articulos;
    TextView textCount;
    Articulo articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_articulos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarListArticulos));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = getSession().getTipo();

        listView = findViewById(R.id.listListArticulos);
        textCount = findViewById(R.id.textCountArticulos);

        cargaListaArticulos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                articulo = articulos.get(position);

                String idArticulo = String.valueOf(articulo.getId());

                Intent intent = new Intent(ListadoArticulos.this, ArticuloSeleccionado.class);
                intent.putExtra("id",idArticulo);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        cargaListaArticulos();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if ("Admin".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_admin, menu);
        } else if ("User".equals(tipoUsuario)) {
            inflater.inflate(R.menu.menu_user, menu);
        }

        return true;
    }

    private void cargaListaArticulos() {
        String url = "https://reynaldomd.com/phpscript/listado_articulos.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    articulos = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            articulos.add(
                                    new Articulo.ArticuloBuilder()
                                            .setId(Long.parseLong(obj.getString("idArticulo")))
                                            .setNombre(obj.getString("nombre"))
                                            .setCategoria(obj.getString("categoria"))
                                            .setPrecio(Double.parseDouble(obj.getString("precio")))
                                            .build()
                            );
                        }

                        adapter = new ListaArticulosAdapter(this, articulos);
                        listView.setAdapter(adapter);
                        textCount.setText("Total de artículos: " + articulos.size());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, volleyError -> {
            Toast.makeText(this, "Error en la carga de datos.", Toast.LENGTH_SHORT).show();
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ListadoArticulos.this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }
}