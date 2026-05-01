package com.fullstack.reporte_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private String tipoIncendio;

    // Identificación (todos pueden ser opcionales dependiendo del caso)
    private Long usuarioId;
    private String runCiudadano;
    private Boolean anonimo;
}
