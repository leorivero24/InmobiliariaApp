package com.example.inmobiliaria.request;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> login(
            @Field("usuario") String usuario,  // <-- minúsculas
            @Field("clave") String clave       // <-- minúsculas
    );
}
