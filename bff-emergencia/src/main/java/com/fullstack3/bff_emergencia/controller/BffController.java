package com.fullstack3.bff_emergencia.controller;

import com.fullstack3.bff_emergencia.dto.*;
import com.fullstack3.bff_emergencia.service.EmergenciaOrchestratorService;
import com.fullstack3.bff_emergencia.service.ResilienteClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody UsuarioRequestDTO request) {
        return orchestrator.loginFuncionario(request);
    }
}
