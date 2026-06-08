package com.fullstack3.bff_emergencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brigada-service", url = "http://localhost:8081/api/v1/brigadas")
public interface BrigadaClient {
    @PostMapping("/api/v1/brigadas/asignar")
    String asignarBrigada(@RequestParam("reporteId") Long reporteId, @RequestParam("tipoEquipo") String tipoEquipo);
}
