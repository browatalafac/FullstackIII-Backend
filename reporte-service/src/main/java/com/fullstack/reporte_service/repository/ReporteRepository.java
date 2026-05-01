package com.fullstack.reporte_service.repository;

import com.fullstack.reporte_service.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
}
