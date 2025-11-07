package com.example.inmobiliaria.ui.perfil;

import android.util.Log;

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

    // 🔹 Getters LiveData
    public LiveData<Propietario> getPropietarioLiveData() { return propietarioLiveData; }
    public LiveData<Boolean> getEditMode() { return editMode; }
    public LiveData<String> getMensaje() { return mensaje; }

    // 🔹 Callback para operaciones de perfil
    public interface PerfilCallback {
        void onSuccess();
        void onError(String error);
    }

    // 🔹 Cargar datos del propietario desde API
    public void cargarDatosPropietario(String token) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Propietario> call = api.obtenerPropietario("Bearer " + token);

        Log.d("PerfilViewModel", "📡 Solicitando datos del propietario...");

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioLiveData.postValue(response.body());
                } else {
                    mensaje.postValue("Error al obtener datos del perfil: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.postValue("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // 🔹 Actualizar datos del propietario (sin tocar tu método original)
    public void actualizarPropietario(String token, String nombre, String apellido, String dni,
                                      String email, String telefono, PerfilCallback callback) {
        Propietario p = new Propietario();
        if (propietarioLiveData.getValue() != null) {
            p.setIdPropietario(propietarioLiveData.getValue().getIdPropietario());
        }
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setDni(dni);
        p.setEmail(email);
        p.setTelefono(telefono);

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Propietario> call = api.actualizarPropietario("Bearer " + token, p);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioLiveData.postValue(response.body());
                    callback.onSuccess();
                } else {
                    callback.onError("Error al actualizar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // 🔹 Cambiar contraseña (sin tocar tu método original)
    public void cambiarPassword(String token, String currentPassword, String newPassword, PerfilCallback callback) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Void> call = api.changePassword("Bearer " + token, currentPassword, newPassword);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error al cambiar contraseña: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // 🔹 Método central para botón Editar/Guardar
    public void onEditarGuardarClicked(String token,
                                       String nombre, String apellido, String dni,
                                       String email, String telefono,
                                       String claveActual, String nuevaClave) {

        if (!Boolean.TRUE.equals(editMode.getValue())) {
            // Activar modo edición
            editMode.setValue(true);
            return;
        }

        // Guardar cambios generales
        actualizarPropietario(token, nombre, apellido, dni, email, telefono, new PerfilCallback() {
            @Override
            public void onSuccess() {
                // Verificar si se quiere cambiar contraseña
                if (!claveActual.isEmpty() && !nuevaClave.isEmpty()) {
                    cambiarPassword(token, claveActual, nuevaClave, new PerfilCallback() {
                        @Override
                        public void onSuccess() {
                            mensaje.postValue("Datos y contraseña actualizados");
                            editMode.postValue(false);
                        }

                        @Override
                        public void onError(String error) {
                            mensaje.postValue("Datos guardados, fallo al cambiar contraseña: " + error);
                            editMode.postValue(false);
                        }
                    });
                } else {
                    mensaje.postValue("Datos actualizados");
                    editMode.postValue(false);
                }
            }

            @Override
            public void onError(String error) {
                mensaje.postValue(error);
            }
        });
    }
}
