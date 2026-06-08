package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.RutaDTO;
import com.fullstack3.bff_emergencia.dto.ZonaRiesgoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "riesgo-service", url = "http://localhost:1015/api/v1/riesgos")
public interface RiesgoClient {
    @GetMapping("/api/v1/riesgos/zona-evacuacion/{reporteId}")
    ZonaRiesgoDTO obtenerZonaEvacuacion(@PathVariable("reporteId") Long reporteId);

    @GetMapping("/api/v1/riesgos/ruta-segura/{reporteId}")
    RutaDTO obtenerRutaSegura(@PathVariable("reporteId") Long reporteId);
}
