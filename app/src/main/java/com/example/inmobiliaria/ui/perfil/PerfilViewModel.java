package com.example.inmobiliaria.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.modelo.Propietario;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<Propietario> propietarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editMode = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private String token;

    // 🔹 Getters LiveData
    public LiveData<Propietario> getPropietarioLiveData() { return propietarioLiveData; }
    public LiveData<Boolean> getEditMode() { return editMode; }
    public LiveData<String> getMensaje() { return mensaje; }

    // 🔹 Inicializar ViewModel con token
    public void inicializar(String token) {
        this.token = token;
        cargarDatosPropietario();
    }

    // 🔹 Cargar datos del propietario
    private void cargarDatosPropietario() {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Propietario> call = api.obtenerPropietario("Bearer " + token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioLiveData.postValue(response.body());
                } else {
                    mensaje.postValue("Error al obtener datos del perfil");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.postValue("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // 🔹 Botón Editar/Guardar
    public void onEditarGuardarClicked(String nombre, String apellido, String dni,
                                       String email, String telefono, String claveActual, String nuevaClave) {

        if (!Boolean.TRUE.equals(editMode.getValue())) {
            editMode.setValue(true); // Activar edición
            return;
        }

        // 🔹 Actualizar datos del propietario
        Propietario p = propietarioLiveData.getValue() != null ? propietarioLiveData.getValue() : new Propietario();
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setDni(dni);
        p.setEmail(email);
        p.setTelefono(telefono);

        actualizarPropietario(p, claveActual, nuevaClave);
    }

    // 🔹 Actualizar propietario en servidor
    private void actualizarPropietario(Propietario p, String claveActual, String nuevaClave) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Propietario> call = api.actualizarPropietario("Bearer " + token, p);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioLiveData.postValue(response.body());
                    editMode.postValue(false);

                    if (!claveActual.isEmpty() && !nuevaClave.isEmpty()) {
                        cambiarPassword(claveActual, nuevaClave);
                    } else {
                        mensaje.postValue("Datos actualizados");
                    }
                } else {
                    mensaje.postValue("Error al actualizar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.postValue(t.getMessage());
            }
        });
    }

    // 🔹 Cambiar contraseña
    private void cambiarPassword(String actual, String nueva) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Void> call = api.changePassword("Bearer " + token, actual, nueva);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mensaje.postValue(response.isSuccessful()
                        ? "Datos y contraseña actualizados"
                        : "Datos guardados, fallo al cambiar contraseña: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.postValue("Datos guardados, fallo al cambiar contraseña: " + t.getMessage());
            }
        });
    }
}
