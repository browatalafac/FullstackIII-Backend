package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.client.UsuarioClient;
import com.fullstack3.bff_emergencia.dto.ReporteRequestDTO;
import com.fullstack3.bff_emergencia.dto.ReporteResponseDTO;
import com.fullstack3.bff_emergencia.dto.UsuarioRequestDTO;
import com.fullstack3.bff_emergencia.dto.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmergenciaOrchestratorService {

    private UsuarioClient usuarioClient;
    private ReporteClient reporteClient;

    public ReporteResponseDTO procesarReporte(ReporteRequestDTO request) {

        if (request.getAnonimo() == null) {
            throw new RuntimeException("El campo anonimo es obligatorio");
        }

        if (request.getLatitud() == null ||
                request.getLongitud() == null ||
                request.getDescripcion() == null ||
                request.getTipoIncendio() == null) {
            throw new RuntimeException("Faltan campos obligatorios");
        }

        // 🔵 SI NO ES ANÓNIMO → validas RUN y CREAS EL USUARIO
        if (!request.getAnonimo()) {

            if (request.getRunCiudadano() == null || request.getRunCiudadano().isBlank()) {
                throw new RuntimeException("run obligatorio si no es anonimo");
            }

            // -------- CÓDIGO NUEVO AÑADIDO --------
            try {
                // Armamos el DTO para el microservicio de usuarios
                UsuarioRequestDTO nuevoUsuario = new UsuarioRequestDTO();
                nuevoUsuario.setRun(request.getRunCiudadano());
                nuevoUsuario.setRol("CIUDADANO"); // Tu servicio de usuario exige un rol obligatorio

                // Llamamos al método que ya tienes para registrarlo
                this.registrarUsuario(nuevoUsuario);

            } catch (Exception e) {
                // Manejo de errores: Si el RUN ya existe en la base de datos,
                // el microservicio de usuarios podría arrojar un error.
                // Aquí puedes decidir si frenar el reporte o dejar que continúe.
                System.out.println("Nota: El usuario ya existe o hubo un error al crearlo - " + e.getMessage());
            }
            // --------------------------------------

        } else {
            request.setRunCiudadano(null);
        }

        return reporteClient.guardarReporte(request);
    }

    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {

        if (request.getRun() == null || request.getRun().isBlank()) {
            throw new RuntimeException("El run es obligatorio");
        }

        if (request.getRol() == null || request.getRol().isBlank()) {
            throw new RuntimeException("El rol es obligatorio");
        }

        return usuarioClient.crearUsuario(request);
    }
}