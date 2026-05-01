package com.fullstack.reporte_service.factory;

import com.fullstack.reporte_service.handler.ReporteHandler;
import com.fullstack.reporte_service.handler.impl.ReporteForestalHandler;
import com.fullstack.reporte_service.handler.impl.ReporteUrbanoHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReporteHandlerFactory {

    private final Map<String, ReporteHandler> handlers = new HashMap<>();
    private final ReporteHandler defaultHandler;

    /**
     * Constructor que inicializa el mapa de handlers
     */

    public ReporteHandlerFactory(
            ReporteForestalHandler reporteForestalHandler,
            ReporteUrbanoHandler reporteUrbanoHandler) {

        handlers.put(reporteForestalHandler.getTipoIncendioSoportado(), reporteForestalHandler);
        handlers.put(reporteUrbanoHandler.getTipoIncendioSoportado(), reporteUrbanoHandler);

        this.defaultHandler = reporteForestalHandler;
    }

    public ReporteHandler getHandler(String tipoIncendio) {
        if (tipoIncendio == null) {
            return defaultHandler;
        }
        return handlers.getOrDefault(tipoIncendio.trim().toUpperCase(), defaultHandler);
    }

}
