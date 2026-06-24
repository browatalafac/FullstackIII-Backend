package com.fullstack3.riesgo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaRiesgoDTO {
    private Long reporteId;
    private String nivelAlerta;
    private List<CoordenadaDTO> perimetro;
}
