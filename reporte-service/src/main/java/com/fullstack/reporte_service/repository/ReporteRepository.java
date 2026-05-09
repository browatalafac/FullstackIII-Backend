package com.fullstack.reporte_service.repository;

import com.fullstack.reporte_service.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    Optional<Reporte> findByCodigoSeguimiento(String codigoSeguimiento);
}
