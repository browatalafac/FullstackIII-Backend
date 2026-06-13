package com.fullstack3.brigadas_recursos_service.controller;

import com.fullstack3.brigadas_recursos_service.dto.BrigadaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/brigadas")
public class BrigadaController {
    // Simulación de base de datos para la PoC
    private List<BrigadaDTO> brigadas = new ArrayList<>();

    @GetMapping("/activas")
    public List<BrigadaDTO> obtenerBrigadasActivas() {
        // Retornaría brigadas con estado DISPONIBLE
        return brigadas;
    }

    @PostMapping("/asignar")
    public ResponseEntity<String> asignarBrigada(@RequestParam Long reporteId, @RequestParam String tipoEquipo) {

        // Lógica: Buscar una brigada de 'tipoEquipo' (ej. BOMBEROS_FORESTALES)
        // que esté disponible, cambiar su estado a OCUPADO y vincularla al reporteId.

        return ResponseEntity.ok("Brigada asignada al reporte: " + reporteId);
    }
}
