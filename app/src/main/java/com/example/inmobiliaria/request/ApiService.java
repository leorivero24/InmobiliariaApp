package com.example.inmobiliaria.request;

import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.modelo.Pago;
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
import retrofit2.http.Path;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;

public interface ApiService {

    // 🔹 LOGIN
    @FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> login(
            @Field("usuario") String usuario,
            @Field("clave") String clave
    );

    // 🔹 OBTENER DATOS DEL PROPIETARIO
    @GET("api/Propietarios")
    Call<Propietario> obtenerPropietario(
            @Header("Authorization") String token
    );

    // 🔹 ACTUALIZAR DATOS DEL PROPIETARIO
    @PUT("api/Propietarios/actualizar")
    Call<Propietario> actualizarPropietario(
            @Header("Authorization") String token,
            @Body Propietario propietario
    );

    // 🔹 CAMBIAR CONTRASEÑA
    @FormUrlEncoded
    @PUT("api/Propietarios/changePassword")
    Call<Void> changePassword(
            @Header("Authorization") String token,
            @Field("currentPassword") String currentPassword,
            @Field("newPassword") String newPassword
    );

    // 🔹 RESETEAR CONTRASEÑA
    @FormUrlEncoded
    @POST("api/Propietarios/email")
    Call<String> enviarEmailRecuperacion(
            @Field("email") String email
    );

    // 🔹 OBTENER TODOS LOS INMUEBLES DEL PROPIETARIO
    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles(
            @Header("Authorization") String token
    );

    // 🔹 Actualizar inmueble
    @PUT("api/Inmuebles/actualizar")
    Call<Inmueble> actualizarInmueble(
            @Header("Authorization") String token,
            @Body Inmueble request
    );

    @Multipart
    @POST("api/Inmuebles/cargar")
    Call<Inmueble> cargarInmueble(
            @Header("Authorization") String token,
            @Part MultipartBody.Part imagen,
            @Part("inmueble") RequestBody inmuebleJson
    );

    // 🔹 Obtener inmuebles con contrato vigente
    @GET("api/Inmuebles/GetContratoVigente")
    Call<List<Inmueble>> obtenerInmueblesAlquilados(
            @Header("Authorization") String token
    );

    // 🔹 Obtener contrato e inquilino por ID de inmueble
    @GET("api/contratos/inmueble/{id}")
    Call<Contrato> obtenerContratoPorInmueble(
            @Header("Authorization") String token,
            @Path("id") int idInmueble
    );

    @GET("api/pagos/contrato/{id}")
    Call<List<Pago>> obtenerPagosPorContrato(
            @Header("Authorization") String token,
            @Path("id") int idContrato
    );
}
