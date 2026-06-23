package com.fullstack3.brigadas_recursos_service.controller;

import com.fullstack3.brigadas_recursos_service.dto.BrigadaDTO;
import com.fullstack3.brigadas_recursos_service.service.BrigadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brigadas")
@RequiredArgsConstructor
public class BrigadaController {

    private final BrigadaService brigadaService;

    @PostMapping("/asignar")
    public ResponseEntity<String> asignarBrigada(@RequestParam Long reporteId, @RequestParam String tipoEquipo) {
        try {
            String resultado = brigadaService.asignarMejorBrigada(reporteId, tipoEquipo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            // Devuelve 503 Service Unavailable si no hay nadie
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }
}