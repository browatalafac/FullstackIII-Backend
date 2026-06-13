package com.fullstack3.notificaciones_service.controller;

import com.fullstack3.notificaciones_service.dto.AlertaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotificacionesControllerTest {

    // Instanciamos el controlador directamente
    private NotificacionController notificacionController;

    @BeforeEach
    void setUp() {
        // Inicializamos un controlador limpio antes del test
        notificacionController = new NotificacionController();
    }

    @Test
    void enviarAlerta_RecibeDtoYRetornaMensajeExitoso() {
        // ARRANGE
        AlertaDTO alertaMock = new AlertaDTO(
                101L,
                "Alerta Temprana: Incendio forestal detectado",
                "ALTA",
                "TODOS"
        );

        // ACT
        ResponseEntity<String> response = notificacionController.enviarAlerta(alertaMock);

        // ASSERT
        assertNotNull(response, "La respuesta no debería ser nula");

        // Verificamos que el código HTTP sea 200
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El código de estado debe ser 200 OK");

        // Verificamos que el mensaje de respuesta sea exactamente el que espera el BFF
        assertEquals("Notificaciones enviadas exitosamente", response.getBody(), "El mensaje del cuerpo debe coincidir");
    }

}