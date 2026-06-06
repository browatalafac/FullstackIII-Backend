package com.fullstack3.riesgo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenadaDTO {
    private Double latitud;
    private Double longitud;
}
