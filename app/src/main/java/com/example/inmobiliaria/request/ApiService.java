package com.example.inmobiliaria.request;
import com.example.inmobiliaria.modelo.Inmueble;

import com.example.inmobiliaria.modelo.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {

    // LOGIN
    @retrofit2.http.FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> login(
            @retrofit2.http.Field("usuario") String usuario,
            @retrofit2.http.Field("clave") String clave
    );

    // OBTENER DATOS DEL PROPIETARIO
    @GET("api/Propietarios")
    Call<Propietario> obtenerPropietario(
            @Header("Authorization") String token
    );

    // ACTUALIZAR DATOS DEL PROPIETARIO
    @PUT("api/Propietarios/actualizar")
    Call<Propietario> actualizarPropietario(
            @Header("Authorization") String token,
            @Body Propietario propietario
    );

    // CAMBIAR CONTRASEÑA
    @retrofit2.http.FormUrlEncoded
    @PUT("api/Propietarios/changePassword")
    Call<Void> changePassword(
            @Header("Authorization") String token,
            @retrofit2.http.Field("currentPassword") String currentPassword,
            @retrofit2.http.Field("newPassword") String newPassword
    );

    // OBTENER TODOS LOS INMUEBLES DEL PROPIETARIO
    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles(
            @Header("Authorization") String token
    );

    // Actualizar inmueble
    @PUT("/api/Inmuebles/actualizar")
    Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble request);

    @Multipart
    @POST("api/Inmuebles/cargar")
    Call<Inmueble> cargarInmueble(
            @Header("Authorization") String token,
            @Part MultipartBody.Part imagen,
            @Part("inmueble") RequestBody inmuebleJson
    );


}
