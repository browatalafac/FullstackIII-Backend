package com.fullstack3.bff_emergencia.controller;

import com.fullstack3.bff_emergencia.dto.*;
import com.fullstack3.bff_emergencia.service.EmergenciaOrchestratorService;
import com.fullstack3.bff_emergencia.service.ResilienteClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/bff/emergencias")
public class BffController {

    private EmergenciaOrchestratorService orchestrator; //Ponerlo final cuando los controladres, con sus servicios esten listos
    private final ResilienteClientService resilienteService;

    @PostMapping("/reportar")
    public ReporteResponseDTO crearReporte(@RequestBody ReporteRequestDTO request) {
        return orchestrator.procesarReporte(request);
    }

    @GetMapping("/reportes")
    public List<ReporteResponseDTO> verTodosLosReportes() {
        return orchestrator.obtenerTodosLosReportes();
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
        //Más adelante, este endpoint deberá exigir que la petición traiga un JWT válido
        // en los Headers para asegurar que solo un funcionario activo pueda crear a otro.
        return orchestrator.registrarNuevoFuncionario(request);
    }



}
