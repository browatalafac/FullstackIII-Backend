package com.fullstack3.bff_emergencia.dto;

import com.fullstack3.bff_emergencia.enums.TipoIncendio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private TipoIncendio tipoIncendio;
}
