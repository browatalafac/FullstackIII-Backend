package com.fullstack3.brigadas_recursos_service.service;

import com.fullstack3.brigadas_recursos_service.model.Brigada;
import com.fullstack3.brigadas_recursos_service.repository.BrigadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrigadaService {
    private final BrigadaRepository repository;

    public String asignarMejorBrigada(Long reporteId, String tipoDeseado) {

        // 1. Intentamos buscar la brigada exacta que pide el orquestador
        Optional<Brigada> brigadaIdeal = repository.findFirstByTipoEquipoAndEstado(tipoDeseado, "DISPONIBLE");

        if (brigadaIdeal.isPresent()) {
            Brigada asignada = brigadaIdeal.get();
            asignada.setEstado("EN_RUTA");
            repository.save(asignada);
            return "ÉXITO: Se asignó la brigada especializada '" + asignada.getNombre() + "' al reporte " + reporteId;
        }

        // 2. Si no hay de las exactas, buscamos una de apoyo general (MIXTO)
        Optional<Brigada> brigadaMixta = repository.findFirstByTipoEquipoAndEstado("MIXTO", "DISPONIBLE");

        if (brigadaMixta.isPresent()) {
            Brigada asignada = brigadaMixta.get();
            asignada.setEstado("EN_RUTA");
            repository.save(asignada);
            return "ADVERTENCIA: Sin equipos " + tipoDeseado + ". Se envió brigada de apoyo '" + asignada.getNombre() + "' al reporte " + reporteId;
        }

        // 3. Si todo el mundo está ocupado
        throw new RuntimeException("CRÍTICO: No hay ninguna brigada disponible (ni especializada ni mixta) para el reporte " + reporteId);
    }
}
