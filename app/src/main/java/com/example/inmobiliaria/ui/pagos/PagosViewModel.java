//MVVM

package com.example.inmobiliaria.ui.pagos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.modelo.Pago;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends ViewModel {

    private final MutableLiveData<List<Pago>> pagosLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public LiveData<List<Pago>> getPagosLiveData() {
        return pagosLiveData;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void cargarPagos(String token, int idContrato) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<List<Pago>> call = api.obtenerPagosPorContrato("Bearer " + token, idContrato);

        Log.d("PagosViewModel", "📡 Solicitando pagos del contrato ID=" + idContrato);

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 🔹 Formatear la fecha antes de publicarla
                    List<Pago> pagosFormateados = response.body().stream()
                            .peek(pago -> pago.setFechaPago(formatearFecha(pago.getFechaPago())))
                            .collect(Collectors.toList());

                    pagosLiveData.postValue(pagosFormateados);
                    Log.d("PagosViewModel", "✅ Pagos cargados correctamente (" + pagosFormateados.size() + ")");
                } else {
                    mensaje.postValue("Error al cargar pagos");
                    Log.e("PagosViewModel", "❌ Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                mensaje.postValue("Fallo en la conexión: " + t.getMessage());
                Log.e("PagosViewModel", "⚠️ Fallo: " + t.getMessage());
            }
        });
    }

    private String formatearFecha(String fechaOriginal) {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat salida = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date fecha = entrada.parse(fechaOriginal);
            return salida.format(fecha);
        } catch (ParseException e) {
            Log.e("PagosViewModel", "Error al formatear fecha: " + fechaOriginal);
            return fechaOriginal;
        }
    }
}
