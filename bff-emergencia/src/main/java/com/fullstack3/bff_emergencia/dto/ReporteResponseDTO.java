package com.fullstack3.bff_emergencia.dto;

import com.fullstack3.bff_emergencia.enums.EquipoAsignado;
import com.fullstack3.bff_emergencia.enums.EstadoReporte;
import com.fullstack3.bff_emergencia.enums.NivelPrioridad;
import com.fullstack3.bff_emergencia.enums.TipoIncendio;
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
    private String codigoSeguimiento;
    private LocalDateTime fechaReporte;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private TipoIncendio tipoIncendio;
    private EstadoReporte estado;
    private NivelPrioridad nivelPrioridad;
    private Integer radioImpacto;
    private EquipoAsignado equipoAsignado;
    private String imagenBase64;
}
