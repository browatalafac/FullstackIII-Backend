package com.fullstack3.bff_emergencia.dto;

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

    private Long usuarioId;
    private String runCiudadano;
    private Boolean anonimo;
}
