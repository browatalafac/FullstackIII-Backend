package com.fullstack3.bff_emergencia.service;

import com.fullstack3.bff_emergencia.client.ReporteClient;
import com.fullstack3.bff_emergencia.client.UsuarioClient;
import com.fullstack3.bff_emergencia.dto.*;
import com.fullstack3.bff_emergencia.enums.EquipoAsignado;
import com.fullstack3.bff_emergencia.enums.EstadoReporte;
import com.fullstack3.bff_emergencia.enums.NivelPrioridad;
import com.fullstack3.bff_emergencia.enums.TipoIncendio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergenciaOrchestratorServiceTest {

    @Mock
    private ReporteClient reporteClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private JwtService jwtService;

    @Mock
    private ResilienteClientService resilienteService;

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


        when(reporteClient.actualizarReporte(idReporte, updateDTO)).thenReturn(mockResponse);

        // ACT
        ReporteResponseDTO resultado = orchestrator.actualizarEstadoReporte(idReporte, updateDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(EstadoReporte.APAGADO, resultado.getEstado());

        // Verificamos que el orquestador no hizo la lógica por su cuenta, sino que se la delegó a Feign
        verify(reporteClient, times(1)).actualizarReporte(idReporte, updateDTO);
    }

    @Test
    void procesarReporte_OrquestaTodosLosServiciosCorrectamente() {
        // ARRANGE
        ReporteRequestDTO request = new ReporteRequestDTO();
        request.setLatitud(-33.45);
        request.setLongitud(-70.65);
        request.setDescripcion("Incendio de prueba");
        request.setTipoIncendio(TipoIncendio.FORESTAL);

        ReporteResponseDTO mockResponse = new ReporteResponseDTO();
        mockResponse.setId(100L);
        mockResponse.setTipoIncendio(TipoIncendio.FORESTAL);
        mockResponse.setNivelPrioridad(NivelPrioridad.ALTA); // Forzamos ALTA para probar brigadas
        mockResponse.setEquipoAsignado(EquipoAsignado.BOMBEROS_FORESTALES);

        // 1. Simulamos que la geolocalización dice que es válido
        when(resilienteService.validarCoordenadas(any(CoordenadaDTO.class))).thenReturn(true);

        // 2. Simulamos que el reporte se guarda correctamente
        when(reporteClient.guardarReporte(request)).thenReturn(mockResponse);

        // ACT
        ReporteResponseDTO resultado = orchestrator.procesarReporte(request);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());

        // Verificamos que el orquestador se comunicó con todos los microservicios esperados
        verify(resilienteService, times(1)).validarCoordenadas(any(CoordenadaDTO.class));
        verify(reporteClient, times(1)).guardarReporte(request);
        verify(resilienteService, times(1)).enviarAlertaSegura(any(AlertaDTO.class));
        verify(resilienteService, times(1)).asignarBrigadaSegura(100L, "BOMBEROS_FORESTALES");
    }

}
