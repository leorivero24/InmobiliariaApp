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

    private MutableLiveData<Propietario> propietarioLiveData = new MutableLiveData<>();

    public LiveData<Propietario> getPropietarioLiveData() {
        return propietarioLiveData;
    }

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
                    Propietario p = response.body();
                    Log.d("PerfilViewModel", "✅ Datos recibidos: " +
                            p.getNombre() + " " + p.getApellido() + " - " + p.getEmail());
                    propietarioLiveData.postValue(p);
                } else {
                    Log.e("PerfilViewModel", "❌ Error en respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PerfilViewModel", "⚠️ Error en la solicitud: " + t.getMessage());
            }
        });
    }

    // 🔹 Actualizar datos del propietario
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

        Log.d("PerfilViewModel", "📤 Enviando actualización del propietario...");

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("PerfilViewModel", "✅ Actualización exitosa");
                    propietarioLiveData.postValue(response.body());
                    callback.onSuccess();
                } else {
                    Log.e("PerfilViewModel", "❌ Error al actualizar: " + response.code());
                    callback.onError("Error al actualizar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("PerfilViewModel", "⚠️ Falló la actualización: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    // 🔹 Cambiar contraseña
    public void cambiarPassword(String token, String currentPassword, String newPassword, PerfilCallback callback) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<Void> call = api.changePassword("Bearer " + token, currentPassword, newPassword);

        Log.d("PerfilViewModel", "🔑 Intentando cambiar contraseña...");

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("PerfilViewModel", "✅ Contraseña cambiada exitosamente");
                    callback.onSuccess();
                } else {
                    Log.e("PerfilViewModel", "❌ Error al cambiar contraseña: " + response.code());
                    callback.onError("Error al cambiar contraseña: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PerfilViewModel", "⚠️ Falló el cambio de contraseña: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }
}
