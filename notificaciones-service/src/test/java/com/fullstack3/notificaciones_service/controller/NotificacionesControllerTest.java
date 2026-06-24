package com.fullstack3.notificaciones_service.controller;

import com.fullstack3.notificaciones_service.dto.AlertaDTO;
import com.fullstack3.notificaciones_service.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NotificacionesControllerTest {
    @Mock
    private EmailService emailServiceMock;

    @InjectMocks
    private NotificacionController notificacionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        doNothing().when(emailServiceMock).enviarCorreo(anyString(), anyString(), anyString());

        // ACT
        ResponseEntity<String> response = notificacionController.enviarAlerta(alertaMock);

        // ASSERT
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El código de estado debe ser 200 OK");
        assertEquals("Notificación enviada por correo exitosamente", response.getBody(), "El mensaje del cuerpo debe coincidir");
        verify(emailServiceMock, times(1)).enviarCorreo(anyString(), anyString(), anyString());
    }

    @Test
    void enviarAlerta_RetornaError500SiElServicioFalla() {
        // ARRANGE
        AlertaDTO alertaMock = new AlertaDTO(101L, "Test", "ALTA", "TODOS");
        doThrow(new RuntimeException("Error simulado de red")).when(emailServiceMock).enviarCorreo(anyString(), anyString(), anyString());

        // ACT
        ResponseEntity<String> response = notificacionController.enviarAlerta(alertaMock);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "El código debe ser 500 si falla el correo");
        assertEquals("Fallo al enviar el correo", response.getBody());
    }
}