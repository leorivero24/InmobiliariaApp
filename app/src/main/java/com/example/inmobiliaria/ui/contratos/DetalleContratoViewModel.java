//MVVM
//SIMPLEDATEFORMAT

package com.example.inmobiliaria.ui.contratos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inquilino;
import com.example.inmobiliaria.modelo.Inmueble;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetalleContratoViewModel extends ViewModel {

    private final MutableLiveData<String> fechaInicio = new MutableLiveData<>();
    private final MutableLiveData<String> fechaFin = new MutableLiveData<>();
    private final MutableLiveData<String> monto = new MutableLiveData<>();
    private final MutableLiveData<String> inquilinoNombre = new MutableLiveData<>();
    private final MutableLiveData<String> direccion = new MutableLiveData<>();
    private final MutableLiveData<Integer> abrirPagos = new MutableLiveData<>();

    private final SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat salida = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private Contrato contrato;

    public LiveData<String> getFechaInicio() { return fechaInicio; }
    public LiveData<String> getFechaFin() { return fechaFin; }
    public LiveData<String> getMonto() { return monto; }
    public LiveData<String> getInquilinoNombre() { return inquilinoNombre; }
    public LiveData<String> getDireccion() { return direccion; }
    public LiveData<Integer> getAbrirPagos() { return abrirPagos; }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;

        if (contrato == null) return;

        fechaInicio.setValue(formatearFecha(contrato.getFechaInicio()));
        fechaFin.setValue(formatearFecha(contrato.getFechaFinalizacion()));
        monto.setValue("$" + contrato.getMontoAlquiler());

        Inquilino inq = contrato.getInquilino();
        inquilinoNombre.setValue(inq != null ? inq.getNombre() + " " + inq.getApellido() : "");

        Inmueble inm = contrato.getInmueble();
        direccion.setValue(inm != null ? inm.getDireccion() : "");
    }

    private String formatearFecha(String fechaOriginal) {
        try {
            return salida.format(entrada.parse(fechaOriginal));
        } catch (ParseException e) {
            return fechaOriginal; // si falla, devolvemos la original
        }
    }

    public void onVerPagosClicked() {
        if (contrato != null) {
            abrirPagos.setValue(contrato.getIdContrato());
        }
    }
}
