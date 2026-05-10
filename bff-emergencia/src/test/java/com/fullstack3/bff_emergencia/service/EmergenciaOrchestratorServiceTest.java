package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.dto.ReporteResponseDTO;
import com.fullstack3.bff_emergencia.dto.ReporteUpdateDTO;
import com.fullstack3.bff_emergencia.enums.EstadoReporte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergenciaOrchestratorServiceTest {
    @Mock
    private ReporteClient reporteClient; // Simulamos la comunicación externa con Feign

    @InjectMocks
    private EmergenciaOrchestratorService orchestrator;

    @Test
    void actualizarEstadoReporte_LlamaAlClienteFeign() {
        // ARRANGE
        Long idReporte = 1L;

        ReporteUpdateDTO updateDTO = new ReporteUpdateDTO();
        updateDTO.setEstado(EstadoReporte.APAGADO);

        ReporteResponseDTO mockResponse = new ReporteResponseDTO();
        mockResponse.setId(idReporte);
        mockResponse.setEstado(EstadoReporte.APAGADO);

        // Le decimos al mock: "Cuando el orquestador llame a Feign, devuelve esto"
        when(reporteClient.actualizarReporte(idReporte, updateDTO)).thenReturn(mockResponse);

        // ACT
        ReporteResponseDTO resultado = orchestrator.actualizarEstadoReporte(idReporte, updateDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(EstadoReporte.APAGADO, resultado.getEstado());

        // Verificamos que el orquestador no hizo la lógica por su cuenta, sino que se la delegó a Feign
        verify(reporteClient, times(1)).actualizarReporte(idReporte, updateDTO);
    }
}
