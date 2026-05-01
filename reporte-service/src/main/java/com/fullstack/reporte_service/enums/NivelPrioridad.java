package com.fullstack.reporte_service.enums;

public enum NivelPrioridad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta"),
    CRITICA("Crítica");

    private final String descripcion;

    NivelPrioridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
