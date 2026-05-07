package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.ReporteRequestDTO;
import com.fullstack3.bff_emergencia.dto.ReporteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reporte-service", url = "http://localhost:8081/api/v1/reportes")
public interface ReporteClient {
    @PostMapping
    ReporteResponseDTO guardarReporte(@RequestBody ReporteRequestDTO request);

    @GetMapping("/{id}")
    ReporteResponseDTO getById(@PathVariable("id") Long id);

    @GetMapping
    List<ReporteResponseDTO> getAllReportes();

}