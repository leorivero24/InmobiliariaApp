//MVVM

package com.example.inmobiliaria.ui.contratos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends ViewModel {

    private final MutableLiveData<List<Contrato>> contratosLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Contrato> mostrarDetalle = new MutableLiveData<>();

    private final ApiService api = ApiClient.getRetrofit().create(ApiService.class);

    public LiveData<List<Contrato>> getContratosLiveData() {
        return contratosLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Contrato> getMostrarDetalle() {
        return mostrarDetalle;
    }

    public void cargarContratos(String token) {
        loading.postValue(true);

        api.obtenerInmueblesAlquilados("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    errorMessage.postValue("Error al obtener inmuebles: " + response.code());
                    loading.postValue(false);
                    return;
                }

                List<Inmueble> inmuebles = response.body();
                if (inmuebles.isEmpty()) {
                    errorMessage.postValue("No hay contratos vigentes.");
                    contratosLiveData.postValue(new ArrayList<>());
                    loading.postValue(false);
                    return;
                }

                List<Contrato> contratosTemp = new ArrayList<>();
                for (Inmueble inmueble : inmuebles) {
                    api.obtenerContratoPorInmueble("Bearer " + token, inmueble.getIdInmueble())
                            .enqueue(new Callback<Contrato>() {
                                @Override
                                public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        contratosTemp.add(response.body());
                                        contratosLiveData.postValue(new ArrayList<>(contratosTemp));
                                    }
                                    loading.postValue(false);
                                }

                                @Override
                                public void onFailure(Call<Contrato> call, Throwable t) {
                                    errorMessage.postValue("Error al obtener contrato: " + t.getMessage());
                                    loading.postValue(false);
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                errorMessage.postValue("Fallo de conexión: " + t.getMessage());
                loading.postValue(false);
            }
        });
    }

    public void onContratoSeleccionado(Contrato contrato) {
        mostrarDetalle.postValue(contrato);
    }
}
