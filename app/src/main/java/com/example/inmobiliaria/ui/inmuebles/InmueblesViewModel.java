package com.example.inmobiliaria.ui.inmuebles;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends ViewModel {

    private MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Inmueble>> getInmueblesLiveData() {
        return inmueblesLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void cargarInmuebles(String token) {
        loading.setValue(true);
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<List<Inmueble>> call = api.obtenerInmuebles("Bearer " + token);

        Log.d("InmueblesViewModel", "📡 Solicitando lista de inmuebles...");

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("InmueblesViewModel", "✅ Inmuebles recibidos: " + response.body().size());
                    inmueblesLiveData.postValue(response.body());
                } else {
                    Log.e("InmueblesViewModel", "❌ Error en respuesta: " + response.code());
                    error.setValue("Error al cargar inmuebles: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                loading.setValue(false);
                Log.e("InmueblesViewModel", "⚠️ Error en la solicitud: " + t.getMessage());
                error.setValue("Fallo en la conexión: " + t.getMessage());
            }
        });
    }
}
