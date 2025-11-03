package com.example.inmobiliaria.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<LatLng> inmobiliariaLocation = new MutableLiveData<>();
    private GoogleMap gMap;

    public InicioViewModel() {
        // Ubicación de la inmobiliaria
        inmobiliariaLocation.setValue(new LatLng(-33.148411904954, -66.307301027387));
    }

    public LiveData<LatLng> getInmobiliariaLocation() {
        return inmobiliariaLocation;
    }

    public void setMap(GoogleMap googleMap) {
        this.gMap = googleMap;
        updateMap();
    }

    private void updateMap() {
        if (gMap != null && inmobiliariaLocation.getValue() != null) {
            gMap.clear();
            LatLng loc = inmobiliariaLocation.getValue();
            gMap.addMarker(new MarkerOptions().position(loc).title("Inmobiliaria ULP"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }
    }

    public void updateLocation(double lat, double lng) {
        inmobiliariaLocation.setValue(new LatLng(lat, lng));
        updateMap();
    }
}
