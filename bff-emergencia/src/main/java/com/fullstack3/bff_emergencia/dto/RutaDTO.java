package com.fullstack3.bff_emergencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaDTO {
    private Long reporteId;
    private String descripcion;
    private List<CoordenadaDTO> puntosRuta;
}
