package com.example.inmobiliaria.modelo;

import java.io.Serializable;

public class Inmueble implements Serializable {

    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private int superficie;
    private double latitud;
    private double longitud;
    private double valor;
    private String imagen;
    private boolean disponible;
    private boolean tieneContratoVigente;
    private Propietario duenio;

    public Inmueble() {}

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }

    public double getSuperficie() { return superficie; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public boolean isTieneContratoVigente() { return tieneContratoVigente; }
    public void setTieneContratoVigente(boolean tieneContratoVigente) { this.tieneContratoVigente = tieneContratoVigente; }

    public Propietario getDuenio() { return duenio; }
    public void setDuenio(Propietario duenio) { this.duenio = duenio; }
}


