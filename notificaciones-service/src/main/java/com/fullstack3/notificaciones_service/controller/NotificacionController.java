package com.fullstack3.notificaciones_service.controller;

import com.fullstack3.notificaciones_service.dto.AlertaDTO;
import com.fullstack3.notificaciones_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final EmailService emailService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarAlerta(@RequestBody AlertaDTO alertaDTO) {

        // 1. Decidir a quién le enviamos el correo basado en el rol.
        // Para pruebas, puedes usar tu propio correo secundario.
        String correoDestino = "bomberos.valledelsol@yopmail.com";

        if (alertaDTO.getDestinatarioRol().equals("COMUNIDAD")) {
            correoDestino = "comunidad.valledelsol@yopmail.com";
        }

        // 2. Armar el Asunto
        String asunto = "ALERTA " + alertaDTO.getPrioridad() + " - Reporte #" + alertaDTO.getReporteId();

        // 3. Armar el Cuerpo del Mensaje
        String cuerpo = "SISTEMA DE ALERTAS MUNICIPALIDAD VALLE DEL SOL\n" +
                "---------------------------------------------------\n\n" +
                "Se ha detectado una nueva emergencia que requiere atención:\n\n" +
                "Detalle: " + alertaDTO.getMensaje() + "\n" +
                "Prioridad: " + alertaDTO.getPrioridad() + "\n" +
                "ID de Incidente: " + alertaDTO.getReporteId() + "\n\n" +
                "Por favor, revise el panel de control inmediatamente."+"\n"+
                "http://localhost:5173/login";

        // 4. Enviar el correo
        try {
            emailService.enviarCorreo(correoDestino, asunto, cuerpo);
            return ResponseEntity.ok("Notificación enviada por correo exitosamente");
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Fallo al enviar el correo");
        }
    }

}
