package com.fullstack.reporte_service.service;

import com.fullstack.reporte_service.dto.ReporteRequestDTO;
import com.fullstack.reporte_service.dto.ReporteResponseDTO;
import com.fullstack.reporte_service.dto.ReporteUpdateDTO;
import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.enums.TipoIncendio;
import com.fullstack.reporte_service.factory.ReporteHandlerFactory;
import com.fullstack.reporte_service.handler.ReporteHandler;
import com.fullstack.reporte_service.model.Reporte;
import com.fullstack.reporte_service.repository.ReporteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReporteService {

    private final ReporteRepository reportesRepository;

    private final ReporteHandlerFactory factory;

    // Obtener todos los reportes convertidos a DTO
    public List<ReporteResponseDTO> obtenerTodos(){
        List<Reporte> reportes = reportesRepository.findAll();

        return reportes.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtiene un reporte por su id
    public ReporteResponseDTO obtenerPorId(Long id) {
        Reporte reporte = reportesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        return mapearAResponseDTO(reporte);
    }


    //Guardar reporte
    public ReporteResponseDTO guardarReporte(ReporteRequestDTO requestDTO) {
        if (requestDTO.getAnonimo() == null) {
            throw new RuntimeException("El campo anonimo es obligatorio");
        }

        if (Boolean.TRUE.equals(requestDTO.getAnonimo())) {
            requestDTO.setUsuarioId(null);
            requestDTO.setRunCiudadano(null);
        } else {
            if (requestDTO.getUsuarioId() == null && (requestDTO.getRunCiudadano() == null || requestDTO.getRunCiudadano().isBlank())) {
                throw new RuntimeException("Si no es anónimo, debes enviar usuarioId o runCiudadano");
            }
        }




        Reporte reporte = new Reporte();
        reporte.setLatitud(requestDTO.getLatitud());
        reporte.setLongitud(requestDTO.getLongitud());
        reporte.setDescripcion(requestDTO.getDescripcion());
        reporte.setTipoIncendio(requestDTO.getTipoIncendio());
        reporte.setUsuarioId(requestDTO.getUsuarioId());
        reporte.setRunCiudadano(requestDTO.getRunCiudadano());
        reporte.setAnonimo(requestDTO.getAnonimo());
        reporte.setEstado(EstadoReporte.PENDIENTE);

        // Aca se usa el factory method
        ReporteHandler handler = factory.getHandler(requestDTO.getTipoIncendio());
        handler.procesarSegunTipo(reporte, requestDTO);


        Reporte reporteGuardado = reportesRepository.save(reporte);
        return mapearAResponseDTO(reporteGuardado);
    }


    public ReporteResponseDTO actualizarReporte(Long id, ReporteUpdateDTO updateDTO) {
        Reporte reporte = reportesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        // SOLO actualizar lo permitido

        if (updateDTO.getDescripcion() != null && !updateDTO.getDescripcion().isBlank()) {
            reporte.setDescripcion(updateDTO.getDescripcion());
        }

        if (updateDTO.getTipoIncendio() != null) {
            reporte.setTipoIncendio(updateDTO.getTipoIncendio());
        }

        if (updateDTO.getEstado() != null) {
            reporte.setEstado(updateDTO.getEstado());
        }


        // Si el tipo cambió, reprocesar con handler
        ReporteHandler handler = factory.getHandler(reporte.getTipoIncendio());
        handler.procesarSegunTipo(reporte, updateDTO);

        Reporte actualizado = reportesRepository.save(reporte);
        return mapearAResponseDTO(actualizado);
    }



    // 3. Método auxiliar de mapeo (Entidad -> DTO)
    private ReporteResponseDTO mapearAResponseDTO(Reporte reporte) {
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();
        responseDTO.setId(reporte.getId());
        responseDTO.setFechaReporte(reporte.getFechaReporte());
        responseDTO.setLatitud(reporte.getLatitud());
        responseDTO.setLongitud(reporte.getLongitud());
        responseDTO.setDescripcion(reporte.getDescripcion());
        responseDTO.setTipoIncendio(reporte.getTipoIncendio());
        responseDTO.setEstado(reporte.getEstado());
        responseDTO.setRunCiudadano(reporte.getRunCiudadano());

        responseDTO.setNivelPrioridad(reporte.getNivelPrioridad());
        responseDTO.setRadioImpacto(reporte.getRadioImpacto());
        responseDTO.setEquipoAsignado(reporte.getEquipoAsignado());

        return responseDTO;
    }

}
