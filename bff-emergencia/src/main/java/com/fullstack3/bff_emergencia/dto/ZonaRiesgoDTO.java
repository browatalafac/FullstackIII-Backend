package com.fullstack3.bff_emergencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ZonaRiesgoDTO {
    private Long reporteId;
    private String nivelAlerta;
    private List<CoordenadaDTO> perimetro;
}


