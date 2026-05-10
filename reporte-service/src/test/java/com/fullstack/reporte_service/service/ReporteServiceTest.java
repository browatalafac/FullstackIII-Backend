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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReporteServiceTest {


    @Mock
    private ReporteHandlerFactory factory;
    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteHandler reporteHandler;
    @InjectMocks
    private ReporteService reporteService;


    @Test
    void actualizarEstado_Exito_CambiaEstadoAGuardado() {
        // ARRANGE
        Long idReporte = 1L;

        Reporte reporteEnBd = new Reporte();
        reporteEnBd.setId(idReporte);
        reporteEnBd.setDescripcion("Incendio en el cerro");
        reporteEnBd.setEstado(EstadoReporte.ACTIVO);
        reporteEnBd.setTipoIncendio(TipoIncendio.FORESTAL);

        // Este es el DTO que manda el Brigadista desde React, vía BFF
        ReporteUpdateDTO updateDTO = new ReporteUpdateDTO();
        updateDTO.setEstado(EstadoReporte.APAGADO);

        Reporte reporteActualizado = new Reporte();
        reporteActualizado.setId(idReporte);
        reporteActualizado.setDescripcion("Incendio en el cerro");
        reporteActualizado.setEstado(EstadoReporte.APAGADO);

        // Simulamos la base de datos, cuando busquen el ID 1, devuelve el ACTIVO. Cuando guarden, devuelve el APAGADO.
        when(reporteRepository.findById(idReporte)).thenReturn(Optional.of(reporteEnBd));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteActualizado);

        when(factory.getHandler(any(TipoIncendio.class))).thenReturn(reporteHandler);
        // ACT
        ReporteResponseDTO resultado = reporteService.actualizarReporte(idReporte, updateDTO);

        // ASSERT
        assertNotNull(resultado);
        // Comprobamos que el estado efectivamente cambio a APAGADO
        assertEquals(EstadoReporte.APAGADO, resultado.getEstado());
        // Comprobamos que la descripción original se mantuvo intacta
        assertEquals("Incendio en el cerro", resultado.getDescripcion());

        // Verificamos que el Service llamo a la base de datos para buscar y luego para guardar
        verify(reporteRepository, times(1)).findById(idReporte);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void actualizarEstado_ReporteNoExiste_LanzaExcepcion() {
        // ARRANGE
        Long idFantasma = 99L;
        ReporteUpdateDTO updateDTO = new ReporteUpdateDTO();
        updateDTO.setEstado(EstadoReporte.APAGADO);

        // Simulamos que la base de datos no encuentra nada
        when(reporteRepository.findById(idFantasma)).thenReturn(Optional.empty());

        // ACT y ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reporteService.actualizarReporte(idFantasma, updateDTO);
        });

        assertEquals("Reporte no encontrado con id: 99", exception.getMessage());
        // Verificamos que se intentó buscar, pero nunca se intentó guardar nada (evita datos corruptos)
        verify(reporteRepository, times(1)).findById(idFantasma);
        verify(reporteRepository, never()).save(any(Reporte.class));
    }

    @Test
    void crearReporte_Exito_GuardaYRetornaDTO() {
        // ARRANGE
        ReporteRequestDTO request = new ReporteRequestDTO();
        request.setDescripcion("Fuego cerca de torres de alta tensión");
        request.setTipoIncendio(TipoIncendio.URBANO);
        request.setLatitud(-33.45);
        request.setLongitud(-70.66);

        Reporte reporteGuardado = new Reporte();
        reporteGuardado.setId(10L); // Simulamos que la DB le dio el ID 10
        reporteGuardado.setDescripcion(request.getDescripcion());
        reporteGuardado.setCodigoSeguimiento("INC-2024-ABC");
        reporteGuardado.setEstado(EstadoReporte.PENDIENTE);

        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteGuardado);
        when(factory.getHandler(any(TipoIncendio.class))).thenReturn(reporteHandler);
        // ACT
        ReporteResponseDTO resultado = reporteService.guardarReporte(request);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("INC-2024-ABC", resultado.getCodigoSeguimiento());
        assertEquals(EstadoReporte.PENDIENTE, resultado.getEstado());

        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }
    @Test
    void obtenerTodos_RetornaListaMapeada() {
        // ARRANGE
        Reporte r1 = new Reporte();
        r1.setId(1L);
        r1.setDescripcion("Incendio 1");

        Reporte r2 = new Reporte();
        r2.setId(2L);
        r2.setDescripcion("Incendio 2");

        when(reporteRepository.findAll()).thenReturn(List.of(r1, r2));

        // ACT
        List<ReporteResponseDTO> lista = reporteService.obtenerTodos();

        // ASSERT
        assertEquals(2, lista.size());
        assertEquals("Incendio 1", lista.get(0).getDescripcion());
        assertEquals("Incendio 2", lista.get(1).getDescripcion());

        verify(reporteRepository, times(1)).findAll();
    }

}
