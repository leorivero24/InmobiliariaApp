package com.example.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Propietario;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;
import com.example.inmobiliaria.ui.menu.MenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String usuario = etEmail.getText().toString().trim();
        String clave = etPassword.getText().toString().trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y clave", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<String> call = api.login(usuario, clave);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();

                    // Guardar token
                    getSharedPreferences("inmobiliaria", MODE_PRIVATE)
                            .edit()
                            .putString("token", token)
                            .apply();

                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    // ✅ Llamada para obtener el perfil del propietario
                    ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
                    Call<Propietario> propietarioCall = apiService.obtenerPropietario("Bearer " + token);

                    propietarioCall.enqueue(new Callback<Propietario>() {
                        @Override
                        public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Propietario p = response.body();

                                // Guardar nombre y correo
                                getSharedPreferences("inmobiliaria", MODE_PRIVATE)
                                        .edit()
                                        .putString("nombre", p.getNombre() + " " + p.getApellido())
                                        .putString("email", p.getEmail())
                                        .apply();
                            }

                            // Ir al menú principal
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Propietario> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Error al obtener datos del propietario", Toast.LENGTH_SHORT).show();

                            // Aun así continuar al menú
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
