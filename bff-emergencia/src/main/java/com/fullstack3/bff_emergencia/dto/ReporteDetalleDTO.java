package com.fullstack3.bff_emergencia.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ReporteDetalleDTO {

    private ReporteResponseDTO reporte;
    private ZonaRiesgoDTO zonaEvacuacion;
    private RutaDTO rutaSegura;

}
