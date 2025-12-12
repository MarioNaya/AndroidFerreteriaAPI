package com.example.mnayaferreteriamysql.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.modelo.Articulo;

import java.util.List;

public class OfertasAdapter extends BaseAdapter {

    private Context context;
    private List<Articulo> ofertas;
    private LayoutInflater inflater;

    public OfertasAdapter(Context context, List<Articulo> ofertas) {
        this.context = context;
        this.ofertas = ofertas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ofertas.size();
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
            convertView = inflater.inflate(R.layout.ofertas_adapter, parent, false);
        }

        Articulo articulo = ofertas.get(position);

        TextView nombre = convertView.findViewById(R.id.textNombreOfertas);
        TextView categoria = convertView.findViewById(R.id.textCategoriaOfertas);
        TextView precio = convertView.findViewById(R.id.textPrecioOfertas);
        TextView stock = convertView.findViewById(R.id.textStockOfertas);

        nombre.setText(articulo.getNombre());
        categoria.setText(articulo.getCategoria());
        precio.setText(String.valueOf(articulo.getPrecio()) + "€");
        stock.setText(String.valueOf(articulo.getStock()) + " unid.");

        return convertView;
    }
}
