package com.fullstack.reporte_service.controller;

import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.dto.ReporteResponseDTO;
import com.fullstack.reporte_service.service.ReporteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1/reportes")
@RestController
public final class ReporteController {

    private final ReporteService reportesService;

    @GetMapping
    public List<ReporteResponseDTO> getAllReportes(){
        return reportesService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ReporteResponseDTO getById(@PathVariable Long id) { return reportesService.obtenerPorId(id); }

    @PostMapping
    public ReporteResponseDTO saveReporte(@RequestBody ReporteRequestDTO requestDTO){
        return reportesService.guardarReporte(requestDTO);
    }

}
