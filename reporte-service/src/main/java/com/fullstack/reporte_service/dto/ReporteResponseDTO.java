package com.fullstack.reporte_service.dto;

import com.fullstack.reporte_service.enums.EquipoAsignado;
import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.enums.NivelPrioridad;
import com.fullstack.reporte_service.enums.TipoIncendio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
}
