package com.fullstack.reporte_service.dto;

import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.enums.TipoIncendio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO implements ReporteBaseDTO {
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private TipoIncendio tipoIncendio;
    private String imagenBase64;
}
