package com.fullstack3.notificaciones_service.controller;

import com.fullstack3.notificaciones_service.dto.AlertaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarAlerta(@RequestBody AlertaDTO alertaDTO) {
        // Dependiendo de alertaDTO.getDestinatarioRol(), enviamos a un canal u otro.
        System.out.println("Enviando alerta de prioridad " + alertaDTO.getPrioridad() +
                " sobre el incidente " + alertaDTO.getReporteId());

        // Aquí iría la integración con AWS SES (correos) o SNS (SMS) más adelante.

        return ResponseEntity.ok("Notificaciones enviadas exitosamente");
    }

}
