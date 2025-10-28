package com.example.inmobiliaria.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void login(String usuario, String clave) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<String> call = api.login(usuario, clave);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenLiveData.postValue(response.body());

                } else {
                    errorLiveData.postValue("Credenciales inválidas");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errorLiveData.postValue("Error de red: " + t.getMessage());
            }
        });
    }
}
