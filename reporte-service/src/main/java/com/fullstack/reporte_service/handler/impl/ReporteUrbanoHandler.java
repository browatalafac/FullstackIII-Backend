package com.fullstack.reporte_service.handler.impl;

import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.enums.EquipoAsignado;
import com.fullstack.reporte_service.enums.NivelPrioridad;
import com.fullstack.reporte_service.handler.ReporteHandler;
import com.fullstack.reporte_service.model.Reporte;
import org.springframework.stereotype.Component;

@Component
public class ReporteUrbanoHandler implements ReporteHandler {
    @Override
    public void procesarSegunTipo(Reporte reporte, ReporteRequestDTO requestDTO) {

        reporte.setNivelPrioridad(NivelPrioridad.MEDIA);
        reporte.setRadioImpacto(1000); // 1km de radio, por ahora dejemoslo estaticos, despues se pueden añadir mas
        //condiciones y variables que determinen el tipo de incendio, como ahora solo es un protoripo, esta bien.
        reporte.setEquipoAsignado(EquipoAsignado.BOMBEROS_URBANOS);

    }
    @Override
    public String getTipoIncendioSoportado() {
        return "URBANO";
    }
}
