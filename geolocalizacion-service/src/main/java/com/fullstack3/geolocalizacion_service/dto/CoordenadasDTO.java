package com.fullstack3.geolocalizacion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenadasDTO {
    private Double latitud;
    private Double longitud;
}
