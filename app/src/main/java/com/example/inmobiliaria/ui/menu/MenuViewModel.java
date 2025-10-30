
package com.example.inmobiliaria.ui.menu;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MenuViewModel extends AndroidViewModel {

    private MutableLiveData<String> nombre;
    private MutableLiveData<String> email;

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getNombre() {
        if (nombre == null) {
            nombre = new MutableLiveData<>();
        }
        return nombre;
    }

    public LiveData<String> getEmail() {
        if (email == null) {
            email = new MutableLiveData<>();
        }
        return email;
    }

    public void cargarDatosPropietario() {
        SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", 0);
        String nombreGuardado = sp.getString("nombre", "Usuario");
        String emailGuardado = sp.getString("email", "correo@ejemplo.com");

        if (nombre != null) nombre.setValue(nombreGuardado);
        if (email != null) email.setValue(emailGuardado);
    }

    public void cerrarSesion() {
        SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", 0);
        sp.edit().clear().apply();
    }
}
