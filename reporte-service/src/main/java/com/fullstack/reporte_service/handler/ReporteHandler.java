package com.fullstack.reporte_service.handler;

import com.fullstack.reporte_service.dto.ReporteBaseDTO;
import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.enums.TipoIncendio;
import com.fullstack.reporte_service.model.Reporte;

public interface ReporteHandler {
    void procesarSegunTipo(Reporte reporte, ReporteBaseDTO dto);
    TipoIncendio getTipoIncendioSoportado();
}
