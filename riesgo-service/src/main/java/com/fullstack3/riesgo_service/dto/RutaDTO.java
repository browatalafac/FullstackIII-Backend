package com.fullstack3.riesgo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaDTO {
    private Long reporteId;
    private String descripcion;
    private List<CoordenadaDTO> puntosRuta;
}
