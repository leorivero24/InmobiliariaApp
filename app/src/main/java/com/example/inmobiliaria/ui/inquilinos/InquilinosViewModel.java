//MVVM
package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.modelo.Inquilino;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Contrato>> contratosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> dialogoMensaje = new MutableLiveData<>();

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Contrato>> getContratosLiveData() {
        return contratosLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getDialogoMensaje() {
        return dialogoMensaje;
    }

    public void cargarContratos(String token) {
        loading.setValue(true);
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        Call<List<Inmueble>> callInmuebles = api.obtenerInmueblesAlquilados("Bearer " + token);
        callInmuebles.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmuebles = response.body();
                    List<Contrato> contratos = new ArrayList<>();

                    for (Inmueble inmueble : inmuebles) {
                        Call<Contrato> callContrato = api.obtenerContratoPorInmueble("Bearer " + token, inmueble.getIdInmueble());
                        callContrato.enqueue(new Callback<Contrato>() {
                            @Override
                            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    contratos.add(response.body());
                                    contratosLiveData.postValue(new ArrayList<>(contratos));
                                }
                            }

                            @Override
                            public void onFailure(Call<Contrato> call, Throwable t) {
                                // error individual ignorado
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                loading.postValue(false);
            }
        });
    }

    // 🔹 Nueva función para manejar clics (toda la lógica del if pasa acá)
    public void seleccionarContrato(Contrato contrato) {
        if (contrato != null && contrato.getInquilino() != null) {
            Inquilino inquilino = contrato.getInquilino();
            String info = "Nombre: " + inquilino.getNombre() + "\n" +
                    "Apellido: " + inquilino.getApellido() + "\n" +
                    "DNI: " + inquilino.getDni() + "\n" +
                    "Teléfono: " + inquilino.getTelefono() + "\n" +
                    "Email: " + inquilino.getEmail();
            dialogoMensaje.setValue(info);
        }
    }
}
