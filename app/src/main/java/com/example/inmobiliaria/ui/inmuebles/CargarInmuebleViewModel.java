//MVVM

package com.example.inmobiliaria.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiService;
import com.google.gson.Gson;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Uri> mUri = new MutableLiveData<>();

    public CargarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getMuri() {
        return mUri;
    }

    public void setMuri(Uri uri) {
        mUri.setValue(uri);
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Log.d("CargarInmuebleVM", "Imagen seleccionada: " + uri);
                mUri.setValue(uri);
            }
        }
    }

    public void cargarInmueble(String direccion, String valor, String tipo, String uso,
                               String ambientes, String superficie, boolean disponible) {

        try {
            // Validaciones básicas
            if (direccion.isEmpty() || valor.isEmpty() || tipo.isEmpty() ||
                    uso.isEmpty() || ambientes.isEmpty() || superficie.isEmpty()) {
                Toast.makeText(getApplication(), "Faltan datos del inmueble", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mUri.getValue() == null) {
                Toast.makeText(getApplication(), "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(valor);
            int amb = Integer.parseInt(ambientes);
            int sup = Integer.parseInt(superficie);

            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setValor(precio);
            inmueble.setTipo(tipo);
            inmueble.setUso(uso);
            inmueble.setAmbientes(amb);
            inmueble.setSuperficie(sup);
            inmueble.setDisponible(disponible);

            // Convertir la imagen en bytes
            byte[] imagen = transformarImagen();
            String inmuebleJson = new Gson().toJson(inmueble);

            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

            ApiService inmoService = ApiClient.getApiInmobiliaria();
            String token = ApiClient.leerToken(getApplication());

            Call<Inmueble> call = inmoService.cargarInmueble("Bearer " + token, imagenPart, inmuebleBody);
            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Inmueble cargado exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al cargar inmueble", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "Valor, superficie y ambientes deben ser numéricos", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mUri.getValue();
            if (uri == null) return new byte[]{};

            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplication(), "No se pudo cargar la imagen seleccionada", Toast.LENGTH_SHORT).show();
            return new byte[]{};
        }
    }
}
