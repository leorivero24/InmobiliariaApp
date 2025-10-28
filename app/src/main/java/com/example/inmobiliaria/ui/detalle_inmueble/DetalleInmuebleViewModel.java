package com.example.inmobiliaria.ui.detalle_inmueble;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Inmueble;
//import com.example.inmobiliaria.modelo.InmuebleUpdateRequest;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Inmueble> inmueble = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    // Getter para LiveData
    public LiveData<Inmueble> getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble i) {
        inmueble.setValue(i);
    }

    public void obtenerInmueble(Bundle inmuebleBundle) {
        Inmueble inmueble = (Inmueble) inmuebleBundle.getSerializable("inmueble");

        if (inmueble != null) {
            this.inmueble.setValue(inmueble);
        }

    }

    // Actualizar solo disponibilidad
    public void actualizarDisponibilidad(boolean disponible) {
        if (inmueble.getValue() == null) return;

        // Creamos un nuevo objeto con solo id y disponible
        Inmueble iActualizar = new Inmueble();
        iActualizar.setIdInmueble(this.inmueble.getValue().getIdInmueble());
        iActualizar.setDisponible(disponible);

        // Convertimos a JSON para ver qué vamos a enviar
        String json = new com.google.gson.Gson().toJson(iActualizar);
        android.util.Log.d("DEBUG_UPDATE", "Objeto que se enviará: " + json);

        String token = ApiClient.leerToken(getApplication());
        android.util.Log.d("DEBUG_UPDATE", "Token usado: " + token);

        Call<Inmueble> call = ApiClient.getApiInmobiliaria().actualizarInmueble("Bearer " + token, iActualizar);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("DEBUG_UPDATE", "Respuesta exitosa: " + new com.google.gson.Gson().toJson(response.body()));
                    //  Actualizamos el LiveData con la respuesta del servidor
                    //    inmueble.setValue(response.body());
                    Toast.makeText(getApplication(), "Inmueble actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error vacío";
                        android.util.Log.e("DEBUG_UPDATE", "Error al actualizar: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplication(), "Error al actualizar el inmueble: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                android.util.Log.e("DEBUG_UPDATE", "Fallo al contactar servidor", t);
                Toast.makeText(getApplication(), "Error al contactar con el servidor: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
