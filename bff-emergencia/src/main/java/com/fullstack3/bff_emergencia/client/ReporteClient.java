package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.ReporteRequestDTO;
import com.fullstack3.bff_emergencia.dto.ReporteResponseDTO;
import com.fullstack3.bff_emergencia.dto.ReporteUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reporte-service", url = "http://localhost:1014/api/v1/reportes")
public interface ReporteClient {
    @PostMapping
    ReporteResponseDTO guardarReporte(@RequestBody ReporteRequestDTO request);

    @GetMapping("/seguimiento/{codigo}")
    ReporteResponseDTO getByCodigo(@PathVariable("codigo") String codigo);

    @GetMapping("/{id}")
    ReporteResponseDTO getById(@PathVariable("id") Long id);

    @GetMapping
    List<ReporteResponseDTO> getAllReportes();

    @PutMapping("/{id}")
    ReporteResponseDTO actualizarReporte(@PathVariable("id") Long id, @RequestBody ReporteUpdateDTO updateDTO);

}