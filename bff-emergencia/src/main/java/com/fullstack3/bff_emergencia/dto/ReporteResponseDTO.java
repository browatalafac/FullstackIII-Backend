package com.fullstack3.bff_emergencia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {
    private Long id;
    private LocalDateTime fechaReporte;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private String tipoIncendio;
    private String estado;
    private String runCiudadano;
    private String nivelPrioridad;
    private Integer radioImpacto;
    private String equipoAsignado;
}
