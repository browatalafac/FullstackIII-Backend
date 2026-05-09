package com.fullstack3.bff_emergencia.dto;

import com.fullstack3.bff_emergencia.enums.EstadoReporte;
import com.fullstack3.bff_emergencia.enums.TipoIncendio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReporteUpdateDTO {
    private String descripcion;
    private TipoIncendio tipoIncendio;
    private EstadoReporte estado;
}
