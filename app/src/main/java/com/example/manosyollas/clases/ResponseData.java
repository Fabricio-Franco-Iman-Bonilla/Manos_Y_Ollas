package com.example.manosyollas.clases;

import com.google.gson.annotations.SerializedName;

public class ResponseData {

    private String frame_base64;
    @SerializedName("predicción")
    private String prediccion;
    @SerializedName("clase")
    private int clase;
    @SerializedName("confianza")
    private float confianza;

    // Getters and setters


    public String getPrediccion() {
        return prediccion;
    }

    public void setPrediccion(String prediccion) {
        this.prediccion = prediccion;
    }

    public int getClase() {
        return clase;
    }

    public void setClase(int clase) {
        this.clase = clase;
    }

    public float getConfianza() {
        return confianza;
    }

    public void setConfianza(float confianza) {
        this.confianza = confianza;
    }
}

