package com.example.manosyollas.clases;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forums")
public class ForumItem {
    public void setForoId(@NonNull Integer foroId) {
        this.foroId = foroId;
    }

    @PrimaryKey
    @NonNull
    private Integer foroId;

    public String getFecCracion() {
        return fecCracion;
    }

    public void setFecCracion(String fecCracion) {
        this.fecCracion = fecCracion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    private String fecCracion;
    private String rol;
    private String title;
    private String description;
    private int iconResId; // ID del recurso para el ícono


    public ForumItem(String title, String description, int iconResId) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }
    public Integer getForoId() {
        return foroId;
    }
}
