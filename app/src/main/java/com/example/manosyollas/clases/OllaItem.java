package com.example.manosyollas.clases;

public class OllaItem {
    protected Integer ollaId;
    protected String nombre;
    protected String description;
    protected String fecCreacion;
    protected String nombreDistrito;
    protected String zona;
    protected String latitud;
    protected String longitud;
    protected String direccion;

    protected int iconResId; // ID del recurso para el ícono
    protected Integer foroId;

    public OllaItem(String nombre,String zona, String direccion,String latitud,String longitud, int iconResId) {
        this.nombre = nombre;
        this.zona = zona;
        this.direccion=direccion;
        this.latitud=latitud;
        this.longitud=longitud;
        this.iconResId = iconResId;
    }
    public Integer getOllaId() {
        return ollaId;
    }

    public void setOllaId(Integer ollaId) {
        this.ollaId = ollaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(String fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public Integer getForoId() {
        return foroId;
    }

    public void setForoId(Integer foroId) {
        this.foroId = foroId;
    }


}
