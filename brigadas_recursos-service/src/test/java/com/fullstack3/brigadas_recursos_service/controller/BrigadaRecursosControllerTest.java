package com.fullstack3.brigadas_recursos_service.controller;

import com.fullstack3.brigadas_recursos_service.dto.BrigadaDTO;
import com.fullstack3.brigadas_recursos_service.service.BrigadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BrigadaRecursosControllerTest {

    @Mock
    private BrigadaService brigadaServiceMock;

    @InjectMocks
    private BrigadaController brigadaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void asignarBrigada_RetornaRespuestaExitosa() {
        // ARRANGE
        Long reporteId = 105L;
        String tipoEquipo = "BOMBEROS_FORESTALES";
        String respuestaEsperada = "BOMBEROS_FORESTALES";

        when(brigadaServiceMock.asignarMejorBrigada(anyLong(), anyString())).thenReturn(respuestaEsperada);

        // ACT
        ResponseEntity<String> response = brigadaController.asignarBrigada(reporteId, tipoEquipo);

        // ASSERT
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El código de estado debe ser 200 OK");
        assertEquals(respuestaEsperada, response.getBody(), "El cuerpo debe coincidir con lo que devuelve el servicio");
    }

    @Test
    void asignarBrigada_RetornaError503SiNoHayBrigadas() {
        // ARRANGE
        Long reporteId = 105L;
        String tipoEquipo = "BOMBEROS_FORESTALES";
        String mensajeError = "CRÍTICO: No hay ninguna brigada disponible";

        when(brigadaServiceMock.asignarMejorBrigada(anyLong(), anyString())).thenThrow(new RuntimeException(mensajeError));

        // ACT
        ResponseEntity<String> response = brigadaController.asignarBrigada(reporteId, tipoEquipo);

        // ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode(), "El código de estado debe ser 503 SERVICE UNAVAILABLE");
        assertEquals(mensajeError, response.getBody(), "El cuerpo de la respuesta debe contener el mensaje exacto de la excepción");
    }

}
