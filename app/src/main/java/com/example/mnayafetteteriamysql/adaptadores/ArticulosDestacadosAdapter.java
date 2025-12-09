package com.example.mnayafetteteriamysql.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mnayafetteteriamysql.R;
import com.example.mnayafetteteriamysql.modelo.Articulo;

import java.util.List;

public class ArticulosDestacadosAdapter extends BaseAdapter {

    private Context context;
    private List<Articulo> articulos;
    private LayoutInflater inflater;

    public ArticulosDestacadosAdapter(Context context, List<Articulo> articulos) {
        this.context = context;
        this.articulos = articulos;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return articulos.size();
    }

    @Override
    public Object getItem(int position) {
        return articulos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.articulos_destacados_adapter, parent, false);
        }

        // Obtener el artículo en esta posición
        Articulo articulo = articulos.get(position);

        // Enlazar las vistas
        TextView nombre = convertView.findViewById(R.id.nombreArticuloDestacado);
        TextView categoria = convertView.findViewById(R.id.categoriaArticuloDestacado);
        TextView descripcion = convertView.findViewById(R.id.descripcionArticulosDestacados);

        // Asignar los datos
        nombre.setText(articulo.getNombre());
        categoria.setText(articulo.getCategoria());
        descripcion.setText(articulo.getDescripcion());

        return convertView;
    }
}
