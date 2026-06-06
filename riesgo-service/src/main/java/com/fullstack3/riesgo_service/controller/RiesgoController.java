package com.fullstack3.riesgo_service.controller;

import com.fullstack3.riesgo_service.dto.CoordenadaDTO;
import com.fullstack3.riesgo_service.dto.RutaDTO;
import com.fullstack3.riesgo_service.dto.ZonaRiesgoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/riesgos")
public class RiesgoController {
    @GetMapping("/zona-evacuacion/{reporteId}")
    public ZonaRiesgoDTO obtenerZonaEvacuacion(@PathVariable Long reporteId) {
        // Para el prototipo, simulamos devolver 4 puntos que forman un cuadrado/polígono
        // alrededor del área del reporte. En la vida real, esto se calcularía geográficamente.
        List<CoordenadaDTO> poligono = Arrays.asList(
                new CoordenadaDTO(-33.4500, -70.6500),
                new CoordenadaDTO(-33.4550, -70.6500),
                new CoordenadaDTO(-33.4550, -70.6400),
                new CoordenadaDTO(-33.4500, -70.6400)
        );

        return new ZonaRiesgoDTO(reporteId, "ZONA DE PELIGRO ALTO - Evacuación Inmediata", poligono);
    }

    @GetMapping("/ruta-segura/{reporteId}")
    public RutaDTO obtenerRutaSegura(@PathVariable Long reporteId) {
        // Simulamos devolver un camino (línea) desde el área de riesgo hasta un punto seguro.
        List<CoordenadaDTO> camino = Arrays.asList(
                new CoordenadaDTO(-33.4550, -70.6400), // Punto de inicio
                new CoordenadaDTO(-33.4600, -70.6350), // Intersección
                new CoordenadaDTO(-33.4650, -70.6300)  // Punto seguro (Ej: Albergue)
        );

        return new RutaDTO(reporteId, "Ruta hacia el albergue sur", camino);
    }

}
