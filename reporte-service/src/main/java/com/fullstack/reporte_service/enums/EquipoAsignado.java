package com.fullstack.reporte_service.enums;

import lombok.Getter;

@Getter
public enum EquipoAsignado {
    BOMBEROS_FORESTALES("Bomberos Forestales"),
    BOMBEROS_URBANOS("Bomberos Urbanos"),
    HAZMAT("Hazmat + Bomberos"),
    BRIGADA_ESPECIAL("Brigada Especial"),
    APOYO_AEREO("Apoyo Aéreo");

    private final String descripcion;

    EquipoAsignado(String descripcion) {
        this.descripcion = descripcion;
    }

}
