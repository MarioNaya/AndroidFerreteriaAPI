package com.example.mnayaferreteriamysql.controladores;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.mnayaferreteriamysql.adaptadores.OfertasAdapter;
import com.example.mnayaferreteriamysql.modelo.Articulo;
import com.example.mnayaferreteriamysql.navegacion.Navegacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Ofertas extends Navegacion {

    private ListView listView;
    private OfertasAdapter adapter;
    private String tipoUsuario;
    private List<Articulo> ofertas;
    private Articulo articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ofertas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbarOfertas));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoUsuario = getSession().getTipo();

        listView = findViewById(R.id.listOfertas);

        cargarOfertas();
    }

    @Override
    protected void onResume() {
        cargarOfertas();
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

    private void cargarOfertas() {

        String url = "https://reynaldomd.com/phpscript/listado_articulos_oferta.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    ofertas = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            ofertas.add(
                                    new Articulo.ArticuloBuilder()
                                            .setNombre(obj.getString("nombre"))
                                            .setCategoria(obj.getString("categoria"))
                                            .setPrecio(Double.parseDouble(obj.getString("precio")))
                                            .setStock(Integer.parseInt(obj.getString("stock")))
                                            .build()
                            );
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    adapter = new OfertasAdapter(this, ofertas);
                    listView.setAdapter(adapter);

                }, volleyError -> {
            Toast.makeText(this, getString(R.string.error_carga_datos), Toast.LENGTH_SHORT).show();
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(Ofertas.this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }
}