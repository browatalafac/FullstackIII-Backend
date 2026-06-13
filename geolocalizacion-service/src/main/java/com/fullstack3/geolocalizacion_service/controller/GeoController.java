package com.fullstack3.geolocalizacion_service.controller;

import com.fullstack3.geolocalizacion_service.dto.CoordenadasDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/geolocalizacion")

public class GeoController {

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarCoordenadas(@RequestBody CoordenadasDTO request) {
        boolean esValido = validar(request.getLatitud(), request.getLongitud());
        if (!esValido) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }

    private boolean validar(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) return false;
        boolean latValida = latitud >= -56.5 && latitud <= -17.5;
        boolean lngValida = longitud >= -75.6 && longitud <= -66.0;

        return latValida && lngValida;
    }

}
