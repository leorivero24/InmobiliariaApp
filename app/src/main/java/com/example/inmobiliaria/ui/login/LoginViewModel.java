package com.example.inmobiliaria.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Propietario;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private static final int SHAKE_THRESHOLD = 1200;

    // LiveData para la UI
    private final MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> recoveryEmailSent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> shakeDetected = new MutableLiveData<>(false);

    // Sensor
    private float lastX, lastY, lastZ;
    private long lastTime;

    private final ApiService apiService;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    // -------------------- Getters --------------------
    public MutableLiveData<String> getTokenLiveData() { return tokenLiveData; }
    public MutableLiveData<String> getErrorLiveData() { return errorLiveData; }
    public MutableLiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public MutableLiveData<Boolean> getRecoveryEmailSent() { return recoveryEmailSent; }
    public MutableLiveData<Boolean> getShakeDetected() { return shakeDetected; }

    // -------------------- Login --------------------
    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            errorLiveData.postValue("Ingrese usuario y clave");
            return;
        }

        apiService.login(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    tokenLiveData.postValue(token);
                    guardarToken(token);
                    obtenerDatosPropietario(token);
                } else {
                    errorLiveData.postValue("Credenciales inválidas");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errorLiveData.postValue("Error: " + t.getMessage());
            }
        });
    }

    private void guardarToken(String token) {
        SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", Context.MODE_PRIVATE);
        sp.edit().putString("token", token).apply();
    }

    private void obtenerDatosPropietario(String token) {
        apiService.obtenerPropietario("Bearer " + token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Propietario p = response.body();
                    SharedPreferences sp = getApplication().getSharedPreferences("inmobiliaria", Context.MODE_PRIVATE);
                    sp.edit()
                            .putString("nombre", p.getNombre() + " " + p.getApellido())
                            .putString("email", p.getEmail())
                            .apply();
                }
                loginSuccess.postValue(true);
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                errorLiveData.postValue("Error al obtener datos del propietario");
                loginSuccess.postValue(true);
            }
        });
    }

    // -------------------- Recuperación de contraseña --------------------
    public void enviarSolicitudRecuperacion(String email) {
        if (email.isEmpty()) {
            errorLiveData.postValue("Por favor ingresa un correo válido");
            return;
        }

        ApiClient.getApiInmobiliaria().enviarEmailRecuperacion(email).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) recoveryEmailSent.postValue(true);
                else errorLiveData.postValue("El email ingresado no existe");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errorLiveData.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    // -------------------- Lógica del sensor --------------------
    public void procesarSensor(float x, float y, float z, long currentTimeMillis) {
        if ((currentTimeMillis - lastTime) > 100) {
            long diffTime = currentTimeMillis - lastTime;
            lastTime = currentTimeMillis;

            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;

            lastX = x;
            lastY = y;
            lastZ = z;

            float speed = Math.abs(deltaX + deltaY + deltaZ) / diffTime * 10000;
            if (speed > SHAKE_THRESHOLD) shakeDetected.postValue(true);
        }
    }

    public void resetShake() {
        shakeDetected.postValue(false);
    }
}
