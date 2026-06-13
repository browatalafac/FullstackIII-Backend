package com.fullstack3.brigadas_recursos_service.controller;

import com.fullstack3.brigadas_recursos_service.dto.BrigadaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BrigadaRecursosControllerTest {

    // Instanciamos el controlador directamente, sin Mockito
    private BrigadaController brigadaController;

    @BeforeEach
    void setUp() {
        // Inicializamos un controlador limpio antes de cada test
        brigadaController = new BrigadaController();
    }

    @Test
    void obtenerBrigadasActivas_RetornaListaInicial() {
        // ACT
        List<BrigadaDTO> resultado = brigadaController.obtenerBrigadasActivas();

        // ASSERT
        assertNotNull(resultado, "La lista de brigadas no debe ser nula");
        // Como la lista inicializada en tu controlador está vacía para la PoC, verificamos que el tamaño sea 0
        assertEquals(0, resultado.size(), "Para esta prueba de concepto, la lista inicial debería estar vacía");
    }

    @Test
    void asignarBrigada_RetornaMensajeDeConfirmacion() {
        // ARRANGE
        Long reporteId = 105L;
        String tipoEquipo = "BOMBEROS_FORESTALES";

        // ACT
        ResponseEntity<String> response = brigadaController.asignarBrigada(reporteId, tipoEquipo);

        // ASSERT
        assertNotNull(response, "La respuesta no debería ser nula");

        // Verificamos que el código HTTP sea 200
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El código de estado debe ser 200 OK");

        // Verificamos que el mensaje arme correctamente el String usando el ID que le pasamos antes, 105
        assertEquals("Brigada asignada al reporte: 105", response.getBody(), "El mensaje del cuerpo debe incluir el ID del reporte");
    }

}
