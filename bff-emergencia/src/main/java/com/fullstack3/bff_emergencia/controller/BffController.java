package com.fullstack3.bff_emergencia.controller;

import com.fullstack3.bff_emergencia.client.RiesgoClient;
import com.fullstack3.bff_emergencia.dto.*;
import com.fullstack3.bff_emergencia.service.EmergenciaOrchestratorService;
import com.fullstack3.bff_emergencia.service.ResilienteClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/bff/emergencias")
@CrossOrigin("*")
public class BffController {

    private final EmergenciaOrchestratorService orchestrator;
    private final RiesgoClient riesgoClient;

    @PostMapping("/reportar")
    public ReporteResponseDTO crearReporte(@RequestBody ReporteRequestDTO request) {
        return orchestrator.procesarReporte(request);
    }

    @GetMapping("/reportes")
    public List<ReporteResponseDTO> verTodosLosReportes() {
        return orchestrator.obtenerTodosLosReportes();
    }

    @GetMapping("/reportes/{id}/detalle")
    public ReporteDetalleDTO verDetalleReporteCompleto(@PathVariable Long id) {
        return orchestrator.obtenerDetalleCompleto(id);
    }

    @PutMapping("/reportes/{id}/estado")
    public ReporteResponseDTO cambiarEstado(
            @PathVariable Long id,
            @RequestBody ReporteUpdateDTO updateDTO) {
        return orchestrator.actualizarEstadoReporte(id, updateDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody UsuarioRequestDTO request) {
        return orchestrator.loginFuncionario(request);
    }

    @PostMapping("/funcionarios")
    public UsuarioResponseDTO crearFuncionario(@RequestBody UsuarioRequestDTO request) {
        return orchestrator.registrarNuevoFuncionario(request);
    }

    @GetMapping("/riesgos/{reporteId}/zona-evacuacion")
    public ResponseEntity<ZonaRiesgoDTO> obtenerZonaEvacuacion(
            @PathVariable Long reporteId,
            @RequestParam double lat,
            @RequestParam double lng) {

        return ResponseEntity.ok(riesgoClient.obtenerZonaEvacuacion(reporteId, lat, lng));
    }

    @GetMapping("/riesgos/{reporteId}/ruta-segura")
    public ResponseEntity<RutaDTO> obtenerRutaSegura(
            @PathVariable Long reporteId,
            @RequestParam double lat,
            @RequestParam double lng) {

        return ResponseEntity.ok(riesgoClient.obtenerRutaSegura(reporteId, lat, lng));
    }

}
