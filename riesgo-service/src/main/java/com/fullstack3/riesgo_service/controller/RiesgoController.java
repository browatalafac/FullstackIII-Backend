package com.fullstack3.riesgo_service.controller;

import com.fullstack3.riesgo_service.dto.CoordenadaDTO;
import com.fullstack3.riesgo_service.dto.RutaDTO;
import com.fullstack3.riesgo_service.dto.ZonaRiesgoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/riesgos")
public class RiesgoController {

    @GetMapping("/zona-evacuacion/{reporteId}")
    public ZonaRiesgoDTO obtenerZonaEvacuacion(
            @PathVariable Long reporteId,
            @RequestParam double lat,
            @RequestParam double lng) {

        double delta = 0.003;

        List<CoordenadaDTO> poligono = Arrays.asList(
                new CoordenadaDTO(lat - delta, lng - delta),
                new CoordenadaDTO(lat - delta, lng + delta),
                new CoordenadaDTO(lat + delta, lng + delta),
                new CoordenadaDTO(lat + delta, lng - delta)
        );

        return new ZonaRiesgoDTO(
                reporteId,
                "ZONA DE PELIGRO ALTO - Evacuación Inmediata",
                poligono
        );
    }

    @GetMapping("/ruta-segura/{reporteId}")
    public RutaDTO obtenerRutaSegura(
            @PathVariable Long reporteId,
            @RequestParam double lat,
            @RequestParam double lng) {

        List<CoordenadaDTO> camino = Arrays.asList(
                new CoordenadaDTO(lat, lng),
                new CoordenadaDTO(lat + 0.005, lng + 0.005),
                new CoordenadaDTO(lat + 0.010, lng + 0.010)
        );

        return new RutaDTO(
                reporteId,
                "Ruta hacia el albergue sur",
                camino
        );
    }


}
