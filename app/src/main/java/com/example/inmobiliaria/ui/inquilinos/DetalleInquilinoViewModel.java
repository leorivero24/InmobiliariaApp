//MVVM

package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Inquilino;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private final MutableLiveData<Inquilino> inquilinoLiveData = new MutableLiveData<>();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getInquilino() {
        return inquilinoLiveData;
    }

    // 5- Recibe el objeto desde el bundle y lo guarda en un LiveData:
    public void cargarDesdeBundle(Bundle args) {
        if (args == null) return;

        Object data = args.getSerializable("inquilino");
        if (data instanceof Inquilino) {
            inquilinoLiveData.setValue((Inquilino) data);
        }
    }
}
