package com.fullstack.reporte_service.handler;

import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.model.Reporte;

public interface ReporteHandler {
    void procesarSegunTipo(Reporte reporte, ReporteRequestDTO requestDTO);
    String getTipoIncendioSoportado();
}
