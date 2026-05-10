package com.fullstack.reporte_service.controller;

import com.fullstack.reporte_service.dto.ReporteResponseDTO;
import com.fullstack.reporte_service.dto.ReporteUpdateDTO;
import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReporteControllerTest {

    @InjectMocks
    ReporteController reporteController;

    @Mock
    ReporteService reporteService;

    @Test
    void obtenerReporte_retorna_ok(){
        // ARRANGE
        Long idBuscado = 1L;
        ReporteResponseDTO mockResponse = new ReporteResponseDTO();
        mockResponse.setId(idBuscado);
        mockResponse.setDescripcion("Incendio de prueba");

        when(reporteService.obtenerPorId(idBuscado)).thenReturn(mockResponse);

        // ACT
        ReporteResponseDTO response = reporteController.getById(idBuscado);

        // ASSERT
        assertNotNull(response);
        assertEquals("Incendio de prueba", response.getDescripcion());
        verify(reporteService, times(1)).obtenerPorId(idBuscado);
        }
    @Test
    void actualizarEstado_RetornaReporteActualizado() {
        // ARRANGE
        Long idActualizar = 1L;
        ReporteUpdateDTO updateDTO = new ReporteUpdateDTO();
        updateDTO.setEstado(EstadoReporte.APAGADO);

        ReporteResponseDTO mockResponse = new ReporteResponseDTO();
        mockResponse.setId(idActualizar);
        mockResponse.setEstado(EstadoReporte.APAGADO);

        when(reporteService.actualizarReporte(idActualizar, updateDTO)).thenReturn(mockResponse);

        // ACT
        ReporteResponseDTO response = reporteController.actualizarReporte(idActualizar, updateDTO);

        // ASSERT
        assertNotNull(response);
        assertEquals(EstadoReporte.APAGADO, response.getEstado());
        verify(reporteService, times(1)).actualizarReporte(idActualizar, updateDTO);
    }

}
