package com.example.mnayaferreteriamysql.navegacion;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mnayaferreteriamysql.user.SessionManager;
import com.example.mnayaferreteriamysql.controladores.Cuenta;
import com.example.mnayaferreteriamysql.controladores.ListadoArticulos;
import com.example.mnayaferreteriamysql.controladores.ListadoUsuarios;
import com.example.mnayaferreteriamysql.controladores.MainActivity;
import com.example.mnayaferreteriamysql.controladores.NuevoArticulo;
import com.example.mnayaferreteriamysql.controladores.NuevoUsuario;
import com.example.mnayaferreteriamysql.R;
import com.example.mnayaferreteriamysql.controladores.Ofertas;

public class Navegacion extends AppCompatActivity {

    protected SessionManager getSession(){
        return SessionManager.getInstance(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();

        if (idItem == R.id.itemNuevoArticuloAdmin){
            Intent i = new Intent(this, NuevoArticulo.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemNuevoUsuarioAdmin){
            Intent i = new Intent(this, NuevoUsuario.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemCuentaAdmin){
            Intent i = new Intent(this, Cuenta.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemCuentaUser){
            Intent i = new Intent(this, Cuenta.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemOfertasAdmin){
            Intent i = new Intent(this, Ofertas.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemOfertasUser){
            Intent i = new Intent(this, Ofertas.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemListArticulosAdmin){
            Intent i = new Intent(this, ListadoArticulos.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemListArticulosUser){
            Intent i = new Intent(this, ListadoArticulos.class);
            startActivity(i);
            return true;
        } else if (idItem == R.id.itemListUsersAdmin){
            Intent i = new Intent(this, ListadoUsuarios.class);
            startActivity(i);
            return true;
        }  else if (idItem == R.id.itemCerrarSesionAdmin){
            getSession().cerrarSesion();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        } else if (idItem == R.id.itemCerrarSesionUser){
            getSession().cerrarSesion();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
