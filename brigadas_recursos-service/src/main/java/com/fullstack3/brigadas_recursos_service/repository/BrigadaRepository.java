package com.fullstack3.brigadas_recursos_service.repository;

import com.fullstack3.brigadas_recursos_service.model.Brigada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrigadaRepository extends JpaRepository<Brigada, Long> {
    Optional<Brigada> findFirstByTipoEquipoAndEstado(String tipoEquipo, String estado);
}
