package com.example.manosyollas.clases;

import java.io.Serializable;

public class Donacion2 implements Serializable {

    private String producto_nombre;
    private String producto_cantidad;
    private String olla_nombre;

    public Donacion2() {
    }

    public Donacion2(String producto_nombre, String producto_cantidad, String olla_nombre) {
        this.producto_nombre = producto_nombre;
        this.producto_cantidad = producto_cantidad;
        this.olla_nombre = olla_nombre;
    }

    public String getProducto_nombre() {
        return producto_nombre;
    }

    public void setProducto_nombre(String producto_nombre) {
        this.producto_nombre = producto_nombre;
    }

    public String getProducto_cantidad() {
        return producto_cantidad;
    }

    public void setProducto_cantidad(String producto_cantidad) {
        this.producto_cantidad = producto_cantidad;
    }

    public String getOlla_nombre() {
        return olla_nombre;
    }

    public void setOlla_nombre(String olla_nombre) {
        this.olla_nombre = olla_nombre;
    }

}
