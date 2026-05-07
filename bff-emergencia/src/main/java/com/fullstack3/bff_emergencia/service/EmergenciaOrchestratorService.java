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

    private  UsuarioClient usuarioClient; //Ponerlo final despues de agregar los controladores que faltan

    private ReporteClient reporteClient; //Ponerlo final despues de agregar los controladores que faltan

    public ReporteResponseDTO procesarReporte(ReporteRequestDTO request) {

        // Validaciones básicas
        if (request.getAnonimo() == null) {
            throw new RuntimeException("El campo anonimo es obligatorio");
        }

        if (request.getLatitud() == null) {
            throw new RuntimeException("La latitud es obligatoria");
        }

        if (request.getLongitud() == null) {
            throw new RuntimeException("La longitud es obligatoria");
        }

        if (request.getDescripcion() == null ) {
            throw new RuntimeException("La descripcion es obligatoria");
        }

        if (request.getTipoIncendio() == null) {
            throw new RuntimeException("El tipoIncendio es obligatorio");
        }

        // Si no es anónimo, debe venir usuarioId
        if (!request.getAnonimo()) {
            if (request.getUsuarioId() == null) {
                throw new RuntimeException("usuarioId es obligatorio cuando anonimo = false");
            }

            UsuarioResponseDTO usuario = usuarioClient.obtenerPorId(request.getUsuarioId());

            if (usuario == null) {
                throw new RuntimeException("No existe el usuario con ID " + request.getUsuarioId());
            }

            request.setRunCiudadano(usuario.getRun());
        } else {
            // Si es anónimo, limpiamos datos de usuario
            request.setUsuarioId(null);
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
