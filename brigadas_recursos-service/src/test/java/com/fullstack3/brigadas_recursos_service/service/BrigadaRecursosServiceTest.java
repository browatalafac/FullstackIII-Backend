package com.fullstack3.brigadas_recursos_service.service;

import com.fullstack3.brigadas_recursos_service.model.Brigada;
import com.fullstack3.brigadas_recursos_service.repository.BrigadaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrigadaRecursosServiceTest {
    @Mock
    private BrigadaRepository brigadaRepository;

    @InjectMocks
    private BrigadaService brigadaService;

    @Test
    void Asignar_brigadas_con_exito(){
        Brigada brigada = new Brigada();
        brigada.setNombre("Brigada Alpha");
        brigada.setTipoEquipo("INCENDIO");
        brigada.setEstado("DISPONIBLE");

        when(brigadaRepository.findFirstByTipoEquipoAndEstado("INCENDIO", "DISPONIBLE"))
                .thenReturn(Optional.of(brigada));

        String resultado = brigadaService.asignarMejorBrigada(1L, "INCENDIO");

        assertTrue(resultado.contains("ÉXITO"));
        assertEquals("EN_RUTA", brigada.getEstado());

        verify(brigadaRepository).save(brigada);
    }
    @Test
    void Asignar_brigada_mixta_cuando_no_hay_brigadas_especializadas() {

        Brigada brigadaMixta = new Brigada();
        brigadaMixta.setNombre("Brigada Mixta");
        brigadaMixta.setTipoEquipo("MIXTO");
        brigadaMixta.setEstado("DISPONIBLE");

        when(brigadaRepository.findFirstByTipoEquipoAndEstado("INCENDIO", "DISPONIBLE"))
                .thenReturn(Optional.empty());

        when(brigadaRepository.findFirstByTipoEquipoAndEstado("MIXTO", "DISPONIBLE"))
                .thenReturn(Optional.of(brigadaMixta));

        String resultado = brigadaService.asignarMejorBrigada(1L, "INCENDIO");

        assertTrue(resultado.contains("ADVERTENCIA"));
        assertEquals("EN_RUTA", brigadaMixta.getEstado());

        verify(brigadaRepository).save(brigadaMixta);
    }

    @Test
    void lanzar_excepcion_cuando_no_hay_brigadas_disponibles() {

        when(brigadaRepository.findFirstByTipoEquipoAndEstado("INCENDIO", "DISPONIBLE"))
                .thenReturn(Optional.empty());

        when(brigadaRepository.findFirstByTipoEquipoAndEstado("MIXTO", "DISPONIBLE"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> brigadaService.asignarMejorBrigada(1L, "INCENDIO")
        );

        assertEquals(
                "CRÍTICO: No hay ninguna brigada disponible (ni especializada ni mixta) para el reporte 1",
                exception.getMessage()
        );

        verify(brigadaRepository, never()).save(any());
    }

}
