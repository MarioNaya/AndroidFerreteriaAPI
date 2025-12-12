package com.example.mnayaferreteriamysql.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.modelo.Usuario;

import java.util.List;

public class ListaUsuariosAdapter extends BaseAdapter {

    private Context context;
    private List<Usuario> usuarios;
    private LayoutInflater inflater;



    public ListaUsuariosAdapter(Context context, List<Usuario> usuarios) {
        this.context = context;
        this.usuarios = usuarios;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return usuarios.size();
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
            convertView = inflater.inflate(R.layout.lista_usuarios_adapter, parent, false);
        }

        Usuario usuario = usuarios.get(position);

        TextView nombre = convertView.findViewById(R.id.textNombreItemUsuario);
        TextView username = convertView.findViewById(R.id.textUserUsuario);
        TextView tipo = convertView.findViewById(R.id.textTipoUsuario);
        ImageView imageView = convertView.findViewById(R.id.imageTipoUsuario);

        nombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
        username.setText(usuario.getUsuario());
        tipo.setText(usuario.getTipo());

        if (usuario.getTipo().equals("Admin")|| usuario.getTipo().equals("Administrador") || usuario.getTipo().equals("admin") || usuario.getTipo().equals("administrador")) {
            imageView.setImageResource(R.drawable.pictogramaadmin);
        } else if (usuario.getTipo().equals("User") || usuario.getTipo().equals("Usuario") || usuario.getTipo().equals("user") || usuario.getTipo().equals("usuario")){
            imageView.setImageResource(R.drawable.pictogramauser);
        } else {
            imageView.setImageResource(R.drawable.pictogramanulo);
        }

        return convertView;
    }
}
