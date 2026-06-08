package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.AlertaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacion-service", url = "http://localhost:8081/api/v1/notificaciones")
public interface NotificacionClient {
    @PostMapping("/api/v1/notificaciones/enviar")
    String enviarAlerta(@RequestBody AlertaDTO alertaDTO);
}
