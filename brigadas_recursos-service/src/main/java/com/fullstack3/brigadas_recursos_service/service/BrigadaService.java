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

        //aca se intenta buscar la brigada exacta que pide el orquestador
        Optional<Brigada> brigadaIdeal = repository.findFirstByTipoEquipoAndEstado(tipoDeseado, "DISPONIBLE");

        if (brigadaIdeal.isPresent()) {
            Brigada asignada = brigadaIdeal.get();
            asignada.setEstado("EN_RUTA");
            repository.save(asignada);
            return "ÉXITO: Se asignó la brigada especializada '" + asignada.getNombre() + "' al reporte " + reporteId;
        }

        //si no se encuentra la brigada exacta, buscamos una de apoyo general (o mixto)
        Optional<Brigada> brigadaMixta = repository.findFirstByTipoEquipoAndEstado("MIXTO", "DISPONIBLE");

        if (brigadaMixta.isPresent()) {
            Brigada asignada = brigadaMixta.get();
            asignada.setEstado("EN_RUTA");
            repository.save(asignada);
            return "ADVERTENCIA: Sin equipos " + tipoDeseado + ". Se envió brigada de apoyo '" + asignada.getNombre() + "' al reporte " + reporteId;
        }

        //si está todo ocupado
        throw new RuntimeException("CRÍTICO: No hay ninguna brigada disponible (ni especializada ni mixta) para el reporte " + reporteId);
    }
}
