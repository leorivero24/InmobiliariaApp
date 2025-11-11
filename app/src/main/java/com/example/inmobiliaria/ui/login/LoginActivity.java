package com.example.inmobiliaria.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.ui.menu.MenuActivity;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        observarViewModel();
        // Evento onClick del Login
        // Se llama al metodo login del ViewModel
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();
            loginViewModel.login(email, pass);
        });

        binding.tvForgotPassword.setOnClickListener(v -> mostrarDialogoRecuperacion());

        // 🔹 Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null)
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            else
                Toast.makeText(this, "No hay sensor de movimiento", Toast.LENGTH_LONG).show();
        }
    }
    // Observador
    private void observarViewModel() {
        loginViewModel.getErrorLiveData().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );
        // Observando el exito del login
        loginViewModel.getLoginSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            }
        });

        loginViewModel.getRecoveryEmailSent().observe(this, sent -> {
            if (sent != null && sent) {
                Toast.makeText(this, "Correo enviado correctamente", Toast.LENGTH_LONG).show();
            }
        });

        loginViewModel.getShakeDetected().observe(this, detected -> {
            if (detected != null && detected) {
                llamarInmobiliaria();
                loginViewModel.resetShake();
            }
        });
    }

    private void mostrarDialogoRecuperacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restablecer contraseña");

        final androidx.appcompat.widget.AppCompatEditText input = new androidx.appcompat.widget.AppCompatEditText(this);
        input.setHint("Ingresa tu correo electrónico");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = input.getText().toString().trim();
            loginViewModel.enviarSolicitudRecuperacion(email);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            loginViewModel.procesarSensor(event.values[0], event.values[1], event.values[2], System.currentTimeMillis());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void llamarInmobiliaria() {
        String numero = "tel:2664553747";
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(numero)));
        Toast.makeText(this, "Llamando a la inmobiliaria...", Toast.LENGTH_SHORT).show();
    }
}
