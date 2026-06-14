package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.*;
import com.fullstack3.bff_emergencia.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ResilienteClientService {

    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;
    private final GeoClient geoClient;
    private final NotificacionClient notificacionClient;
    private final BrigadaClient brigadaClient;
    private final RiesgoClient riesgoClient;

    @CircuitBreaker(name = "usuario-service", fallbackMethod = "fallbackObtenerUsuario")
    public UsuarioResponseDTO obtenerUsuario(Long id) {
        return usuarioClient.obtenerPorId(id);
    }

    public UsuarioResponseDTO fallbackObtenerUsuario(Long id, Throwable ex) {
        UsuarioResponseDTO fallbackUsuario = new UsuarioResponseDTO();
        fallbackUsuario.setId(id);
        fallbackUsuario.setEmail("servicio-offline@bomberos.cl");
        return fallbackUsuario;
    }

    @CircuitBreaker(name = "reporte-service", fallbackMethod = "fallbackObtenerReporte")
    public ReporteResponseDTO obtenerReporte(Long id) {
        return reporteClient.getById(id);
    }

    public ReporteResponseDTO fallbackObtenerReporte(Long id, Throwable ex) {
        return ReporteResponseDTO.builder()
                .id(id)
                .descripcion("Servicio de reportes no disponible")
                .build();
    }

    @CircuitBreaker(name = "geo-service", fallbackMethod = "fallbackValidarCoordenadas")
    public Boolean validarCoordenadas(CoordenadaDTO coordenadas) {
        return geoClient.validarCoordenadas(coordenadas);
    }

    public Boolean fallbackValidarCoordenadas(CoordenadaDTO coordenadas, Throwable ex) {

        // Si el servicio respondió con 4xx → las coordenadas son inválidas, NO es emergencia
        if (ex instanceof feign.FeignException feignEx && feignEx.status() >= 400 && feignEx.status() < 500) {
            System.err.println("Coordenadas rechazadas por el geo-service: " + ex.getMessage());
            return false; // ← rechaza el reporte correctamente
        }

        // Solo si el servicio está genuinamente caído (sin conexión) → modo emergencia
        System.err.println("Servicio de geolocalización caído. Asumiendo coordenadas válidas por emergencia.");
        return true;
    }

    @CircuitBreaker(name = "notificacion-service", fallbackMethod = "fallbackEnviarAlerta")
    public void enviarAlertaSegura(AlertaDTO alertaDTO) {
        notificacionClient.enviarAlerta(alertaDTO);
    }

    public void fallbackEnviarAlerta(AlertaDTO alertaDTO, Throwable ex) {
        System.err.println("No se pudo enviar alerta al servicio de notificaciones para el reporte: " + alertaDTO.getReporteId());
    }

    @CircuitBreaker(name = "riesgo-service", fallbackMethod = "fallbackObtenerZona")
    public ZonaRiesgoDTO obtenerZonaEvacuacion(Long reporteId, double lat, double lng) {
        return riesgoClient.obtenerZonaEvacuacion(reporteId, lat, lng);
    }

    public ZonaRiesgoDTO fallbackObtenerZona(Long reporteId, double lat, double lng, Throwable ex) {
        return new ZonaRiesgoDTO(reporteId, "Servicio de riesgos no disponible", new ArrayList<>());
    }

    @CircuitBreaker(name = "riesgo-service", fallbackMethod = "fallbackObtenerRuta")
    public RutaDTO obtenerRutaSegura(Long reporteId, double lat, double lng) {
        return riesgoClient.obtenerRutaSegura(reporteId, lat, lng);
    }

    public RutaDTO fallbackObtenerRuta(Long reporteId, double lat, double lng, Throwable ex) {
        return new RutaDTO(reporteId, "Ruta no disponible temporalmente", new ArrayList<>());
    }

    @CircuitBreaker(name = "brigada-service", fallbackMethod = "fallbackAsignarBrigada")
    public void asignarBrigadaSegura(Long reporteId, String tipoEquipo) {
        brigadaClient.asignarBrigada(reporteId, tipoEquipo);
    }

    public void fallbackAsignarBrigada(Long reporteId, String tipoEquipo, Throwable ex) {
        System.err.println("No se pudo asignar brigada automáticamente al reporte: " + reporteId);
    }
}
