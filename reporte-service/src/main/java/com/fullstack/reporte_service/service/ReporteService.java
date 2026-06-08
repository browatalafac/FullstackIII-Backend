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

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReporteService {

    private final ReporteRepository reportesRepository;
    private final ReporteHandlerFactory factory;

    public List<ReporteResponseDTO> obtenerTodos(){
        List<Reporte> reportes = reportesRepository.findAll();
        return reportes.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    public ReporteResponseDTO obtenerPorId(Long id) {
        Reporte reporte = reportesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        return mapearAResponseDTO(reporte);
    }

    public ReporteResponseDTO obtenerPorCodigo(String codigo) {
        Reporte reporte = reportesRepository.findByCodigoSeguimiento(codigo)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún reporte con ese código."));
        return mapearAResponseDTO(reporte);
    }

    public ReporteResponseDTO guardarReporte(ReporteRequestDTO requestDTO) {

        Reporte reporte = new Reporte();
        reporte.setLatitud(requestDTO.getLatitud());
        reporte.setLongitud(requestDTO.getLongitud());
        reporte.setDescripcion(requestDTO.getDescripcion());
        reporte.setTipoIncendio(requestDTO.getTipoIncendio());
        reporte.setEstado(EstadoReporte.PENDIENTE);

        // --- LÓGICA DE IMAGEN ---
        if (requestDTO.getImagenBase64() != null && !requestDTO.getImagenBase64().isBlank()) {
            try {
                // Si el frontend envía el prefijo (ej. "data:image/png;base64,..."), lo limpiamos
                String base64Data = requestDTO.getImagenBase64();
                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];
                }
                // Decodificamos de String a byte[]
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                reporte.setImagen(decodedBytes);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Error al procesar la imagen enviada.");
            }
        }

        // Uso del factory method
        ReporteHandler handler = factory.getHandler(requestDTO.getTipoIncendio());
        handler.procesarSegunTipo(reporte, requestDTO);

        Reporte reporteGuardado = reportesRepository.save(reporte);
        return mapearAResponseDTO(reporteGuardado);
    }

    public ReporteResponseDTO actualizarReporte(Long id, ReporteUpdateDTO updateDTO) {
        Reporte reporte = reportesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));

        if (updateDTO.getDescripcion() != null && !updateDTO.getDescripcion().isBlank()) {
            reporte.setDescripcion(updateDTO.getDescripcion());
        }

        if (updateDTO.getTipoIncendio() != null) {
            reporte.setTipoIncendio(updateDTO.getTipoIncendio());
        }

        if (updateDTO.getEstado() != null) {
            reporte.setEstado(updateDTO.getEstado());
        }

        ReporteHandler handler = factory.getHandler(reporte.getTipoIncendio());
        handler.procesarSegunTipo(reporte, updateDTO);

        Reporte actualizado = reportesRepository.save(reporte);
        return mapearAResponseDTO(actualizado);
    }

    private ReporteResponseDTO mapearAResponseDTO(Reporte reporte) {
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();
        responseDTO.setId(reporte.getId());
        responseDTO.setCodigoSeguimiento(reporte.getCodigoSeguimiento());
        responseDTO.setFechaReporte(reporte.getFechaReporte());
        responseDTO.setLatitud(reporte.getLatitud());
        responseDTO.setLongitud(reporte.getLongitud());
        responseDTO.setDescripcion(reporte.getDescripcion());
        responseDTO.setTipoIncendio(reporte.getTipoIncendio());
        responseDTO.setEstado(reporte.getEstado());
        responseDTO.setNivelPrioridad(reporte.getNivelPrioridad());
        responseDTO.setRadioImpacto(reporte.getRadioImpacto());
        responseDTO.setEquipoAsignado(reporte.getEquipoAsignado());

        // --- LÓGICA DE IMAGEN ---
        if (reporte.getImagen() != null) {
            // Convertimos de byte[] a String Base64 para enviarlo al frontend
            String encodedString = Base64.getEncoder().encodeToString(reporte.getImagen());
            // Le agregamos el prefijo para que el frontend pueda renderizarlo directamente en una etiqueta <img>
            responseDTO.setImagenBase64("data:image/jpeg;base64," + encodedString);
        }

        return responseDTO;
    }

}
