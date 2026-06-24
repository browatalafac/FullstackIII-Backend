package com.fullstack.reporte_service.handler.impl;

import com.fullstack.reporte_service.dto.ReporteBaseDTO;
import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.enums.EquipoAsignado;
import com.fullstack.reporte_service.enums.NivelPrioridad;
import com.fullstack.reporte_service.enums.TipoIncendio;
import com.fullstack.reporte_service.handler.ReporteHandler;
import com.fullstack.reporte_service.model.Reporte;
import org.springframework.stereotype.Component;

@Component
public class ReporteForestalHandler implements ReporteHandler {
    @Override
    public void procesarSegunTipo(Reporte reporte, ReporteBaseDTO dto) {
        reporte.setNivelPrioridad(NivelPrioridad.ALTA);
        reporte.setRadioImpacto(5000); // 5km de radio
        reporte.setEquipoAsignado(EquipoAsignado.BOMBEROS_FORESTALES);
    }

    @Override
    public TipoIncendio getTipoIncendioSoportado() {
        return TipoIncendio.FORESTAL;
    }
}
