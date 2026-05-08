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


    // MÉTODO GUARDAR DRÁSTICAMENTE SIMPLIFICADO
    public ReporteResponseDTO guardarReporte(ReporteRequestDTO requestDTO) {

        Reporte reporte = new Reporte();
        reporte.setLatitud(requestDTO.getLatitud());
        reporte.setLongitud(requestDTO.getLongitud());
        reporte.setDescripcion(requestDTO.getDescripcion());
        reporte.setTipoIncendio(requestDTO.getTipoIncendio());
        reporte.setEstado(EstadoReporte.PENDIENTE);

        // Uso del factory method para lógicas especiales (radio, prioridad)
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

        return responseDTO;
    }

}
