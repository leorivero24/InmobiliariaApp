//MVVM

package com.example.inmobiliaria.ui.menu;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.ui.contratos.ContratosFragment;
import com.example.inmobiliaria.ui.inicio.InicioFragment;
import com.example.inmobiliaria.ui.inmuebles.InmueblesFragment;
import com.example.inmobiliaria.ui.inquilinos.InquilinosFragment;
import com.example.inmobiliaria.ui.login.LoginActivity;
import com.example.inmobiliaria.ui.perfil.PerfilFragment;

public class MenuViewModel extends AndroidViewModel {

    private MutableLiveData<String> nombre = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<Fragment> fragmentSeleccionado = new MutableLiveData<>();
    private MutableLiveData<String> titulo = new MutableLiveData<>();
    private MutableLiveData<Boolean> mostrarDialogoLogout = new MutableLiveData<>();
    private MutableLiveData<Boolean> irALogin = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getNombre() { return nombre; }
    public LiveData<String> getEmail() { return email; }
    public LiveData<Fragment> getFragmentSeleccionado() { return fragmentSeleccionado; }
    public LiveData<String> getTitulo() { return titulo; }
    public LiveData<Boolean> getMostrarDialogoLogout() { return mostrarDialogoLogout; }
    public LiveData<Boolean> getIrALogin() { return irALogin; }
    public LiveData<String> getMensajeToast() { return mensajeToast; }

    public void cargarDatosPropietario() {
        SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", 0);
        nombre.postValue(sp.getString("nombre", "Usuario"));
        email.postValue(sp.getString("email", "correo@ejemplo.com"));
    }

    // 🔹 Manejo de navegación
    public void seleccionarMenu(int id) {
        Fragment fragment = null;
        String tituloNuevo = "";
        String mensaje = null;

        if (id == R.id.nav_inicio) {
            fragment = new InicioFragment();
            tituloNuevo = "Ubicación";
            mensaje = "Ubicación";
        } else if (id == R.id.nav_perfil) {
            fragment = new PerfilFragment();
            tituloNuevo = "Perfil";
            mensaje = "Perfil";
        } else if (id == R.id.nav_inmuebles) {
            fragment = new InmueblesFragment();
            tituloNuevo = "Inmuebles";
            mensaje = "Inmuebles";
        } else if (id == R.id.nav_inquilinos) {
            fragment = new InquilinosFragment();
            tituloNuevo = "Inquilinos";
            mensaje = "Inquilinos";
        } else if (id == R.id.nav_contratos) {
            fragment = new ContratosFragment();
            tituloNuevo = "Contratos";
            mensaje = "Contratos";
        } else if (id == R.id.nav_logout) {
            mostrarDialogoLogout.postValue(true);
            return;
        }

        if (fragment != null) {
            fragmentSeleccionado.postValue(fragment);
            titulo.postValue(tituloNuevo);
        }

        if (mensaje != null) {
            mensajeToast.postValue(mensaje);
        }
    }

    public void cerrarSesion() {
        SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", 0);
        sp.edit().clear().apply();
        irALogin.postValue(true);
    }
}
