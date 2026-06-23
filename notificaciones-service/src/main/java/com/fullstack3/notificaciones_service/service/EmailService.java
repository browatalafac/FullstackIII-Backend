package com.fullstack3.notificaciones_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();

        // Cambiamos el correo de Gmail por el sistema oficial (inventado)
        mensaje.setFrom("alertas-automaticas@valledelsol.cl");
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        mailSender.send(mensaje);

        System.out.println("Correo atrapado por Mailtrap exitosamente para: " + destinatario);
    }
}
