package com.example.mnayaferreteriamysql.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.modelo.Articulo;

import java.util.List;

public class ListaArticulosAdapter extends BaseAdapter {

    private Context context;
    private List<Articulo> articulos;
    private LayoutInflater inflater;

    public ListaArticulosAdapter(Context context, List<Articulo> articulos) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_articulos_adapter, parent, false);
        }

        Articulo articulo = articulos.get(position);

        TextView nombre = convertView.findViewById(R.id.textNombreListArticulos);
        TextView categoria = convertView.findViewById(R.id.textCategoriaListArticulos);
        TextView precio = convertView.findViewById(R.id.textPrecioListArticulos);
        ImageView imagen = convertView.findViewById(R.id.imageCategoria);

        nombre.setText(articulo.getNombre());
        categoria.setText(articulo.getCategoria());
        precio.setText(String.valueOf(articulo.getPrecio()));

        if (articulo.getCategoria().equals("Herramientas")){
            imagen.setImageResource(R.drawable.pictogramaherramientas);
        } else if (articulo.getCategoria().equals("Menaje")) {
            imagen.setImageResource(R.drawable.pictogramamenaje);
        } else if (articulo.getCategoria().equals("Jardin") || articulo.getCategoria().equals("Jardín")) {
            imagen.setImageResource(R.drawable.pictogramajardin);
        } else if (articulo.getCategoria().equals("Iluminacion") || articulo.getCategoria().equals("Iluminación")) {
            imagen.setImageResource(R.drawable.pictogramailuminacion);
        } else {
            imagen.setImageResource(R.drawable.pictogramanulo);
        }

        return convertView;
    }
}
