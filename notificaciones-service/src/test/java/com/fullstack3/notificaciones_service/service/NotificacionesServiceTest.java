package com.fullstack3.notificaciones_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NotificacionesServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void enviarCorreo_deberiaConstruirYEnviarElMensaje() {

        // Act
        emailService.enviarCorreo(
                "usuario@correo.com",
                "Incendio detectado",
                "Se detectó un incendio forestal."
        );

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensaje = captor.getValue();

        assertEquals("alertas-automaticas@valledelsol.cl", mensaje.getFrom());
        assertEquals("usuario@correo.com", mensaje.getTo()[0]);
        assertEquals("Incendio detectado", mensaje.getSubject());
        assertEquals("Se detectó un incendio forestal.", mensaje.getText());
    }

}
