package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.client.UsuarioClient;
import com.fullstack3.bff_emergencia.dto.ReporteResponseDTO;
import com.fullstack3.bff_emergencia.dto.UsuarioResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResilienteClientService {

    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;

    @CircuitBreaker(name = "usuario-service", fallbackMethod = "fallbackObtenerUsuario")
    public UsuarioResponseDTO obtenerUsuario(Long id) {
        return usuarioClient.obtenerPorId(id);
    }

    public UsuarioResponseDTO fallbackObtenerUsuario(Long id, Exception ex) {
        // Retornar respuesta por defecto o error adaptada a los nuevos campos
        UsuarioResponseDTO fallbackUsuario = new UsuarioResponseDTO();
        fallbackUsuario.setId(id);
        fallbackUsuario.setEmail("servicio-offline@bomberos.cl");
        return fallbackUsuario;
    }

    @CircuitBreaker(name = "reporte-service", fallbackMethod = "fallbackObtenerReporte")
    public ReporteResponseDTO obtenerReporte(Long id) {
        return reporteClient.getById(id);
    }

    public ReporteResponseDTO fallbackObtenerReporte(Long id, Exception ex) {
        return ReporteResponseDTO.builder()
                .id(id)
                .descripcion("Servicio de reportes no disponible")
                .build();
    }

}
