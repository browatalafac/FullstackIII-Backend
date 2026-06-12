package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.client.UsuarioClient;
import com.fullstack3.bff_emergencia.dto.*;
import com.fullstack3.bff_emergencia.enums.NivelPrioridad;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmergenciaOrchestratorService {

    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;
    private final JwtService jwtService;

    // Inyectamos el servicio resiliente que maneja todas las llamadas a los nuevos microservicios
    private final ResilienteClientService resilienteService;

    // Crear reporte con orquestación completa
    public ReporteResponseDTO procesarReporte(ReporteRequestDTO request) {

        if (request.getLatitud() == null ||
                request.getLongitud() == null ||
                request.getDescripcion() == null ||
                request.getTipoIncendio() == null) {
            throw new RuntimeException("Faltan campos obligatorios para el reporte");
        }

        // 1. Validar Geolocalización (Llamada al geo-service)
        CoordenadaDTO coordenadas = new CoordenadaDTO(request.getLatitud(), request.getLongitud());
        Boolean esValido = resilienteService.validarCoordenadas(coordenadas);
        if (Boolean.FALSE.equals(esValido)) {
            throw new RuntimeException("Las coordenadas proporcionadas no son válidas");
        }

        // 2. Guardar el reporte (Llamada al reporte-service)
        ReporteResponseDTO reporteGuardado = reporteClient.guardarReporte(request);

        // 3. Sistema de Alertas (Llamada al notificaciones-service)
        AlertaDTO alerta = new AlertaDTO(
                reporteGuardado.getId(),
                "Nuevo incendio detectado: " + reporteGuardado.getTipoIncendio(),
                reporteGuardado.getNivelPrioridad().name(),
                "TODOS"
        );
        resilienteService.enviarAlertaSegura(alerta);

        // 4. Asignación automática de Brigadas (Llamada al brigada-service)
        // Solo si la prioridad calculada por el Handler fue ALTA
        if (NivelPrioridad.ALTA.equals(reporteGuardado.getNivelPrioridad()) && reporteGuardado.getEquipoAsignado() != null) {
            resilienteService.asignarBrigadaSegura(reporteGuardado.getId(), reporteGuardado.getEquipoAsignado().name());
        }

        return reporteGuardado;
    }

    // NUEVO: Método para empaquetar el reporte junto con los polígonos y rutas del mapa
    public ReporteDetalleDTO obtenerDetalleCompleto(Long reporteId) {
        // Obtenemos los datos base
        ReporteResponseDTO reporte = reporteClient.getById(reporteId);

        // Validar que el reporte tenga coordenadas
        if (reporte.getLatitud() == null || reporte.getLongitud() == null) {
            throw new RuntimeException("El reporte no tiene coordenadas válidas");
        }

        double lat = reporte.getLatitud();
        double lng = reporte.getLongitud();

        // Obtenemos los datos geoespaciales usando la ubicación real del reporte
        ZonaRiesgoDTO zona = resilienteService.obtenerZonaEvacuacion(reporteId, lat, lng);
        RutaDTO ruta = resilienteService.obtenerRutaSegura(reporteId, lat, lng);

        return new ReporteDetalleDTO(reporte, zona, ruta);
    }

    public List<ReporteResponseDTO> obtenerTodosLosReportes() {
        return reporteClient.getAllReportes();
    }

    public ReporteResponseDTO actualizarEstadoReporte(Long id, ReporteUpdateDTO updateDTO) {
        return reporteClient.actualizarReporte(id, updateDTO);
    }

    public UsuarioResponseDTO registrarNuevoFuncionario(UsuarioRequestDTO request) {
        return usuarioClient.crearFuncionarioAdmin(request);
    }

    // Login Funcionarios
    public AuthResponseDTO loginFuncionario(UsuarioRequestDTO request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        // Se llama al microservicio de usuarios para que valide las credenciales
        UsuarioResponseDTO usuarioValidado = usuarioClient.login(request);

        // Se genera el Token JWT usando el servicio inyectado
        String token = jwtService.generarToken(
                usuarioValidado.getEmail(),
                usuarioValidado.getRol().name()
        );

        return new AuthResponseDTO(token, usuarioValidado);
    }
}