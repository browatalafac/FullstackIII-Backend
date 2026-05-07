package com.fullstack.reporte_service.dto;

import com.fullstack.reporte_service.enums.EquipoAsignado;
import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.enums.NivelPrioridad;
import com.fullstack.reporte_service.enums.TipoIncendio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteUpdateDTO implements ReporteBaseDTO {
    private String descripcion;
    private TipoIncendio tipoIncendio;
    private EstadoReporte estado;

}
