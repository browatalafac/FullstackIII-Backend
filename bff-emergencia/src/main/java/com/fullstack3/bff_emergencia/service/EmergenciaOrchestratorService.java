package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.client.UsuarioClient;
import com.fullstack3.bff_emergencia.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmergenciaOrchestratorService {

    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;
    private final JwtService jwtService;

    // 1. CREAR REPORTE (Público y 100% Anónimo)
    public ReporteResponseDTO procesarReporte(ReporteRequestDTO request) {

        if (request.getLatitud() == null ||
                request.getLongitud() == null ||
                request.getDescripcion() == null ||
                request.getTipoIncendio() == null) {
            throw new RuntimeException("Faltan campos obligatorios para el reporte");
        }

        // Ya no hay validaciones de RUN ni creación de usuarios.
        // Simplemente tomamos los datos geográficos y descriptivos y los enviamos.
        return reporteClient.guardarReporte(request);
    }

    public List<ReporteResponseDTO> obtenerTodosLosReportes() {
        return reporteClient.getAllReportes();
    }

    public UsuarioResponseDTO registrarNuevoFuncionario(UsuarioRequestDTO request) {
        // Aquí podrías agregar lógica extra de validación en el BFF si quisieras
        return usuarioClient.crearFuncionarioAdmin(request);
    }



    // 2. LOGIN DE FUNCIONARIO
    public AuthResponseDTO loginFuncionario(UsuarioRequestDTO request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        // 1. Llamamos al microservicio de usuarios para que valide las credenciales
        UsuarioResponseDTO usuarioValidado = usuarioClient.login(request);

        // 2. Generamos el Token JWT usando el servicio inyectado
        String token = jwtService.generarToken(
                usuarioValidado.getEmail(),
                usuarioValidado.getRol().name() // Asumiendo que Rol es un Enum
        );

        // 3. Devolvemos el DTO completo con el token y los datos
        return new AuthResponseDTO(token, usuarioValidado);
    }

    public ReporteResponseDTO actualizarEstadoReporte(Long id, ReporteUpdateDTO updateDTO) {
        // Podrías agregar validaciones extra aquí si lo deseas
        return reporteClient.actualizarReporte(id, updateDTO);
    }
}